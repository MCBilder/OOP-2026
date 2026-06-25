package auction;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AuctionTest {
    @Test
    public void placeBidHigher(){
        Auction auction = new Auction("car", 100);
        boolean result = auction.placeBid("mirek", 110);
        Assertions.assertTrue(result);
    }

    @Test
    public void placeBidLower(){
        Auction auction = new Auction("car", 100);
        boolean result = auction.placeBid("mirek", 50);
        Assertions.assertFalse(result);
    }
}
