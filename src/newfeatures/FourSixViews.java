package newfeatures;

import android.widget.TextView;

public class FourSixViews {

	private TextView fourTV;
	private TextView sixTV;

	public FourSixViews(TextView fourTV, TextView sixTV) {
		this.fourTV = fourTV;
		this.sixTV = sixTV;
	}

	public TextView getFourTV() {
		return fourTV;
	}

	public TextView getSixTV() {
		return sixTV;
	}

}
