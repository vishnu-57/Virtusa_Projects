public class UtilityBill implements Billable {

	String customerName;
	int prevReading;
	int currReading;
	int units;

	UtilityBill(String name, int prev, int curr) {
		customerName = name;
		prevReading = prev;
		currReading = curr;
		units = curr - prev;
	}

	double getBaseCharge() {
		double charge = 0;

		if (units <= 100) {
			charge = units * 1.00;
		} else if (units <= 300) {
			charge = (100 * 1.00) + ((units - 100) * 2.00);
		} else {
			charge = (100 * 1.00) + (200 * 2.00) + ((units - 300) * 5.00);
		}

		return charge;
	}

	double getTax() {
		return getBaseCharge() * 0.10;
	}

	public double calculateTotal() {
		return getBaseCharge() + getTax();
	}
}
