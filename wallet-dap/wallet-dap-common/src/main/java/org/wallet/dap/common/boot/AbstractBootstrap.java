package org.wallet.dap.common.boot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Objects;

/**
 * @author zengfucheng
 * @date 2018年7月31日
 */
public abstract class AbstractBootstrap {
	
	private static final String CLOSED_SECRET_KEY = "!23%46*6)(&5^%$%";
	public static final String PROJECT_PATH = AbstractBootstrap.class.getResource("/").getPath();
	public static final String DEFAULT_CLOSING_LISTENER_PORT_PATH = PROJECT_PATH + "port";
	public static final String FLAG_FILE_FULL_PATH = PROJECT_PATH + "flag";
	private Class<?> clazz;
	private String[] args;
	private String serviceName;
	private String closingListenerPort;
	
	public AbstractBootstrap() {
		closingListenerPort = System.getProperty("org.wallet.service.port");
		if(StringUtils.isEmpty(closingListenerPort)) {
            closingListenerPort = DEFAULT_CLOSING_LISTENER_PORT_PATH;
        }
	}

	protected void sendClosingSignal() {
		try {
			new ServiceClosedHandler().sendSignal();
		} catch (Exception e) {
			LoggerFactory.getLogger(clazz).error("关闭服务失败！", e);
		} 
	}
	
	public void start() {
		Logger logger = LoggerFactory.getLogger(clazz);
		
		if(Objects.isNull(serviceName)){
			logger.info("服务启动中...");
		} else {
			logger.info("[{}] 服务启动中...", serviceName);
		}
		try {
	        startContainers(clazz, args);
			// 用于判断是否启动成功
	        new StartupCloseWriter().write("0");
	        if(Objects.isNull(serviceName)) {
	        	logger.info("服务启动成功！");
	        } else {
	        	logger.info("[{}] 服务启动成功！", serviceName);
	        }
	        new ServiceClosedWaiter(clazz, serviceName).waitingClose();
		} catch(Throwable e) {
			try {
				// 用于判断是否启动成功
				new StartupCloseWriter().write("-1");
			} catch (Exception e1) {} 
			if(Objects.isNull(serviceName)) {
				logger.info("服务启动失败！", e);
			} else {
				logger.error("[{}] 服务启动失败！", serviceName, e);
			}
			System.exit(-1);
		}
	}
	
	public void close() {
		Logger logger = LoggerFactory.getLogger(clazz);
		if(Objects.isNull(serviceName)){
			logger.info("服务关闭中...");
		} else {
			logger.info("[{}] 服务关闭中...", serviceName);
		}
		try {
			closeContainers();
			// 用于判断是否关闭成功
			new StartupCloseWriter().write("0");
	        if(Objects.isNull(serviceName)) {
	        	logger.info("服务已关闭！");
	        } else {
	        	logger.info("[{}] 服务已关闭！", serviceName);
	        }
			// 安全关闭JVM
	        System.exit(0);
		} catch(Throwable e) {
			try {
				// 用于判断是否关闭成功
				new StartupCloseWriter().write("-1");
			} catch (Exception e1) {} 
			if(Objects.isNull(serviceName)) {
				logger.info("服务关闭失败！", e);
			} else {
				logger.error("[{}] 服务关闭失败！", serviceName, e);
			}
			// 异常关闭JVM
			System.exit(-1);
			// 强制关闭JVM
			Runtime.getRuntime().halt(-1);
		}
	}
	
	public Class<?> getClazz() {
		return clazz;
	}

	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}

	public String[] getArgs() {
		return args;
	}

	public void setArgs(String[] args) {
		this.args = args;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	protected abstract void startContainers(Class<?> clazz, String[] args);
	
	protected abstract void closeContainers();
	
	public class ServiceClosedWaiter {
		private ServerSocket serverSocket;
		private int port;
		Logger logger = LoggerFactory.getLogger(getClass());
		
		public ServiceClosedWaiter(Class<?> clazz, String serviceName) throws IOException {
			port = genRandomPort();
			this.serverSocket = new ServerSocket(port);
			logger.info("Service closing the listening port: {}", port);
		}
		
		public void waitingClose() throws IOException {
			new ServiceClosedWriter(port).write();
			while(true) {
				Socket socket = serverSocket.accept();
				BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				String info =null;
				if((info=br.readLine()) != null){
					if(info.equals(CLOSED_SECRET_KEY)) {
						close();
						break;
					} 
				} 
				socket.shutdownInput();
				br.close();
				socket.close();
			}
			serverSocket.close();
		}
	}
	
	private static int genRandomPort() {
		return (int)(Math.random() * 49999);
	}
	
	public class ServiceClosedHandler {
		private Socket socket;
		public ServiceClosedHandler() throws Exception {
			socket = new Socket("127.0.0.1", new ServiceClosedReader().read());
		}

		public void sendSignal() throws IOException {
			PrintWriter pw = new PrintWriter(socket.getOutputStream());
			pw.write(CLOSED_SECRET_KEY);
			pw.flush();
			socket.shutdownOutput();
			pw.close();
			socket.close();
		}
	}
	
	class ServiceClosedWriter {
		private int port;
		private RandomAccessFile randomAccessFile;
		
		public ServiceClosedWriter(int port) throws FileNotFoundException {
			randomAccessFile = new RandomAccessFile(closingListenerPort, "rw");
			this.port = port;
		}

		public void write() throws IOException {
			MappedByteBuffer out = randomAccessFile.getChannel().map(FileChannel.MapMode.READ_WRITE,0,4);
			out.putInt(port);
			randomAccessFile.close();
		}
	}
	
	class ServiceClosedReader {
		private RandomAccessFile randomAccessFile;
		
		public ServiceClosedReader() throws FileNotFoundException {
			randomAccessFile = new RandomAccessFile(closingListenerPort, "r");
		}
		
		public int read() throws IOException {
			FileChannel channel = randomAccessFile.getChannel();
			long size = channel.size();
			MappedByteBuffer out = randomAccessFile.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, size);
			return out.getInt();
		}
	}

	static class StartupCloseWriter {
		private FileWriter fileWriter;
		public StartupCloseWriter() throws IOException {
			File file = new File(FLAG_FILE_FULL_PATH);
			if(!file.exists()) {
				file.createNewFile();
			}
			fileWriter = new FileWriter(file);
		}
		
		public void write(String value) throws IOException {
			fileWriter.write(value);
			fileWriter.flush();
			fileWriter.close();
		}
	}
	 
}
