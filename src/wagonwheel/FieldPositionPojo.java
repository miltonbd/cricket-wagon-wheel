package wagonwheel;

import com.j256.ormlite.field.DatabaseField;

public class FieldPositionPojo {
	@DatabaseField(generatedId = true)
	int id;
	@DatabaseField(index = true)
	String name;
	@DatabaseField
	float x;
	@DatabaseField
	float y;
	@DatabaseField
	int isSelected=0;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public boolean getIsSelected() {
		return isSelected==1?true:false;
	}

	public void setIsSelected(int isSelected) {
		this.isSelected = isSelected;
	}
}
