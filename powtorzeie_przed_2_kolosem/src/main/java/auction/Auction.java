package auction;

public class Auction {
    private String name;
    private double startPrice;
    private double maxBid;
    private String login;

    public Auction(String name, double startPrice) {
        this.name = name;
        this.startPrice = startPrice;
        this.maxBid = startPrice;
        this.login = null;
    }

    public synchronized boolean placeBid(String bidderLogin, double amount){
        if(maxBid < amount){
            login = bidderLogin;
            maxBid = amount;
            return true;
        }else{
            return false;
        }
    }

    public String getLogin() {
        return login;
    }

    public double getMaxBid() {
        return maxBid;
    }

    public String getName() {
        return name;
    }
}
