package org.wallet.dap.common.utils;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Hashtable;

/**
 * 条形码和二维码编码解码
 * @author zengfucheng
 */
public class ZxingUtil {
	private static Logger logger = LoggerFactory.getLogger(ZxingUtil.class);

	/**
	 * 条形码编码
	 * 
	 * @param contents
	 * @param width
	 * @param height
	 * @param imgPath
	 */
	public static void encodeBarCode(String contents, int width, int height,
			String imgPath) {
		// 编码长度 = (start guard) + (left bars) + (middle guard) + (right bars) + (end guard)
		int codeWidth = 3 + (7 * 6) + 5 + (7 * 6) + 3;
		codeWidth = Math.max(codeWidth, width);
		try {
			BitMatrix bitMatrix = new MultiFormatWriter().encode(contents,
					BarcodeFormat.EAN_13, codeWidth, height, null);

			MatrixToImageWriter.writeToPath(bitMatrix, "png", new File(imgPath).toPath());

		} catch (Exception e) {
			logger.error("encodeBarCode error:" + e.getMessage(), e);
		}
	}

	/**
	 * 条形码解码
	 * 
	 * @param imgPath
	 * @return String
	 */
	public static String decodeBarCode(String imgPath) {
		BufferedImage image;
		Result result;
		try {
			image = ImageIO.read(new File(imgPath));
			if (image == null) {
				logger.warn("the decode image may be not exit!");
				throw new IllegalArgumentException("the decode image may be not exit!");
			}
			LuminanceSource source = new BufferedImageLuminanceSource(image);
			BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

			result = new MultiFormatReader().decode(bitmap, null);
			return result.getText();
		} catch (Exception e) {
			logger.error("decodeBarCode error:" + e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 二维码编码
	 * 
	 * @param contents
	 * @param width
	 * @param height
	 * @param imgPath
	 */
	public static void encodeQRCode(String contents, int width, int height,
			String imgPath) {
		Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
		// 指定纠错等级
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
		// 指定编码格式
		hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		// 设置白边大小
		hints.put(EncodeHintType.MARGIN, 0);
		try {
			BitMatrix bitMatrix = new MultiFormatWriter().encode(contents,
					BarcodeFormat.QR_CODE, width, height, hints);

			MatrixToImageWriter.writeToPath(bitMatrix, "png", new File(imgPath).toPath());

		} catch (Exception e) {
			logger.error("encodeQRCode error:" + e.getMessage(), e);
		}
	}

	/**
	 * 二维码编码
	 *
	 * @param contents
	 * @param width
	 * @param height
	 * @return
	 */
	public static BufferedImage encodeQRCode(String contents, int width, int height) {
		Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
		// 指定纠错等级
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
		// 指定编码格式
		hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		// 设置白边大小
		hints.put(EncodeHintType.MARGIN, 0);
		try {

			BitMatrix bitMatrix = new MultiFormatWriter().encode(contents,
					BarcodeFormat.QR_CODE, width, height, hints);

			return MatrixToImageWriter.toBufferedImage(bitMatrix);

		} catch (Exception e) {
			logger.error("encodeQRCode error:" + e.getMessage(), e);
		}

		return null;
	}

	/**
	 * 二维码解码
	 * 
	 * @param imgPath
	 * @return String
	 */
	public static String decodeQRCode(String imgPath) {
		BufferedImage image;
		Result result;
		try {
			image = ImageIO.read(new File(imgPath));
			if (image == null) {
				logger.warn("bufferedImage can not be null!");
				throw new IllegalArgumentException("the decode image may be not exit!");
			}
			LuminanceSource source = new BufferedImageLuminanceSource(image);
			BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

			Hashtable<DecodeHintType, Object> hints = new Hashtable<>();
			hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");

			result = new MultiFormatReader().decode(bitmap, hints);
			return result.getText();
		} catch (Exception e) {
			logger.error("decodeQRCode error:" + e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 二维码解码
	 *
	 * @param bufferedImage img
	 * @return 解码内容
	 */
	public static String decodeQRCode(BufferedImage bufferedImage) {
		Result result;
		try {
			if (bufferedImage == null) {
				logger.warn("bufferedImage can not be null!");
				throw new IllegalArgumentException("bufferedImage can not be null!");
			}
			LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
			BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

			Hashtable<DecodeHintType, Object> hints = new Hashtable<>();
			hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");

			result = new MultiFormatReader().decode(bitmap, hints);
			return result.getText();
		} catch (Exception e) {
			logger.error("decodeQRCode error:" + e.getMessage(), e);
		}
		return null;
	}
}