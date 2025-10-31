public class PayrollService {

    public static void generatePayslip(double basic) {
        double hra = basic * 0.20;
        double da = basic * 0.10;
        double pf = basic * 0.12;
        double professionalTax = 200.0;
        double gross = basic + hra + da;
        double taxable = gross - pf - professionalTax;
        double tax = computeTax(taxable);
        double net = gross - pf - professionalTax - tax;

        System.out.println("\n--- Payslip ---");
        System.out.printf("Basic: %.2f\nHRA: %.2f\nDA: %.2f\nGross: %.2f\nPF: %.2f\nProf. Tax: %.2f\nTax: %.2f\nNet Pay: %.2f\n",
                basic, hra, da, gross, pf, professionalTax, tax, net);
    }

    private static double computeTax(double monthly) {
        double annual = monthly * 12;
        double tax;
        if (annual <= 250000) tax = 0;
        else if (annual <= 500000) tax = (annual - 250000) * 0.05;
        else if (annual <= 1000000) tax = 12500 + (annual - 500000) * 0.20;
        else tax = 112500 + (annual - 1000000) * 0.30;
        return tax / 12;
    }
}
