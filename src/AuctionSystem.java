import java.util.Scanner;

public class AuctionSystem {
    private static AuthenticationService authService = new AuthenticationService();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("Welcome to the Auction System!");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Logout");
            System.out.println("4. Exit");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    authService.register();
                    break;
                case 2:
                    if (authService.login()) {
                        User user = authService.getLoggedInUser();
                        if (user.getRole().equals("AUCTIONEER")) {
                            Auctioneer auctioneer = new Auctioneer(user.getId(), user.getUsername());
                            auctioneer.createAuction();
                        } else if (user.getRole().equals("BIDDER")) {
                            Bidder bidder = new Bidder(user.getId(), user.getUsername());
                            System.out.println("Enter auction ID to place a bid:");
                            int auctionId = scanner.nextInt();
                            bidder.placeBid(auctionId);
                        }
                    }
                    break;
                case 3:
                    authService.logout();
                    break;
                case 4:
                    running = false;
                    System.out.println("Exiting the system. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice, please try again.");
            }
        }
    }
}
