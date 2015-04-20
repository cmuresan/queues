package queuesSimulator;

public class Cashier {

	private float speed;
	private String name;
	
	
	public Cashier(String name, float givenSpeed) {
		this.setName(name);
		speed = givenSpeed;
	}

	public float getSpeed() {
		return speed;
	}

	public void changeSpeed(float newSpeed) {
		speed = newSpeed;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
