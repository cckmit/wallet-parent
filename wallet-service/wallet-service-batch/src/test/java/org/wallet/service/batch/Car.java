package org.wallet.service.batch;

/**
 * @author zengfucheng
 **/
public class Car {
    private String name;
    private Integer age;
    private int flag;

    public Car(){}
    
    public Car(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	@Override
	public String toString() {
		return "Car [name=" + name + ", age=" + age + ", flag=" + flag + "]";
	}
    
    
}
