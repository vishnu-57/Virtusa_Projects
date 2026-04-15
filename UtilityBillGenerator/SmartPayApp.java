import java.util.Scanner;

public class SmartPayApp {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.println("==============================");
		System.out.println("   SmartPay Utility Billing   ");
		System.out.println("==============================");

		while (true) {
			System.out.print("\nEnter Customer Name (Exit to quit): ");
			String name = sc.nextLine();
			if (name.equalsIgnoreCase("Exit")) {
				System.out.println("Exiting SmartPay. Bye!");
				break;
			}
			System.out.print("Enter Previous Meter Reading: ");
			int prev = Integer.parseInt(sc.nextLine());
			System.out.print("Enter Current Meter Reading: ");
			int curr = Integer.parseInt(sc.nextLine());

			if (prev > curr) {
				System.out.println("Error: Previous reading cannot be greater than current reading.");
				continue;
			}

			UtilityBill bill = new UtilityBill(name, prev, curr);

			System.out.println("\n------ Digital Receipt ------");
			System.out.println("Customer Name  : " + bill.customerName);
			System.out.println("Units Consumed : " + bill.units);
			System.out.printf("Base Charge    : $%.2f%n", bill.getBaseCharge());
			System.out.printf("Tax Amount     : $%.2f%n", bill.getTax());
			System.out.printf("Final Total    : $%.2f%n", bill.calculateTotal());
			System.out.println("-----------------------------");
		}

		sc.close();
	}
}
