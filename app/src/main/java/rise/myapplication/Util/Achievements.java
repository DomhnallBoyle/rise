package rise.myapplication.Util;


import rise.myapplication.UI.DisplayToast;
import rise.myapplication.World.GameObjects.Player;

/**
 * Created by Malachy on 17/02/2016.
 */
public class Achievements {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    private final int COINS_ACHIEVEMENT_FOR_COLLECTING_ONE_COIN = 1;
    private final int KILLS_ACHIEVEMENT_FOR_SHOOTING_ONE_ENEMY = 1;
    private final int SCORE_ACHIEVEMENT_1 =1000;
    private final int COINS_ACHIEVEMENT_FOR_COLLECTING_TEN_COIN=10;
    private final int KILLS_ACHIEVEMENT_FOR_SHOOTING_TEN_ENEMY=10;
    private final int SCORE_ACHIEVEMENT_2=2000;
    private final int COINS_ACHIEVEMENT_FOR_COLLECTING_FIFTY_COIN=50;
    private final int KILLS_ACHIEVEMENT_FOR_SHOOTING_TWENTY_ENEMY=20;
    private final int SCORE_ACHIEVEMENT_3=3000;

    private final String gainFirstCoin="First Coin Achievement Achieved";
    private final String gainFirstEnemy="First Kill Achievement Achieved";
    private final String gainScoreAchievement1="1000 Points Achievement Achieved";
    private final String gainTenthCoin="Ten Coins Achievement Achievement";
    private final String gainTenthEnemy="Ten Enemies Killed Achievement";
    private final String gainScoreAchievement2="2000 Points Achievement Achieved";
    private final String gainFiftyCoin="Fifty Coin Achievement Achieved";
    private final String gainTwentyEnemy="Fifty Kills Achievement Achieved";
    private final String gainScoreAchievement3="3000 Points Achievement Achieved";

    private boolean coinAchievementAchieved=false;
    private boolean killsAchievementAchieved=false;
    private boolean scoreAchievement1Achieved=false;
    private boolean coinAchievement1Achieved=false;
    private boolean killsAchievement1Achieved=false;
    private boolean scoreAchievement2Achieved=false;
    private boolean coinAchievement2Achieved=false;
    private boolean killsAchievement2Achieved=false;
    private boolean scoreAchievement3Achieved=false;

    // /////////////////////////////////////////////////////////////////////////
    // Constructor
    // /////////////////////////////////////////////////////////////////////////

    public Achievements(){

    }

    //checking if the player has accomplished any achievements
    public void checkAchievements(Player player)
    {

        //if the player HASN'T got the coin achievement AND the player's number of coins is greater than or equal to the COINS_ACHIEVEMENT_FOR_COLLECTING_ONE_COIN
        if (!coinAchievementAchieved && player.getNumOfCoins() >= COINS_ACHIEVEMENT_FOR_COLLECTING_ONE_COIN){
            coinAchievementAchieved=true;
            player.getGameScreen().getGame().getSharedPreferences().saveAchievement("First Coin Achievement Achieved", true);
            new DisplayToast(gainFirstCoin).execute();
        }

        //if the player HASN'T got the kill achievement AND the player's number of enemies killed is greater than or equal to the KILLS_ACHIEVEMENT_FOR_SHOOTING_ONE_ENEMY
        if (!killsAchievementAchieved && player.getEnemiesKilled() >= KILLS_ACHIEVEMENT_FOR_SHOOTING_ONE_ENEMY){
            killsAchievementAchieved=true;
            player.getGameScreen().getGame().getSharedPreferences().saveAchievement("First Kill Achievement Achieved", true);
            new DisplayToast(gainFirstEnemy).execute();
        }

        //if the player HASN'T got the score achievement 1 AND the player's score value is greater than or equal to the SCORE_ACHIEVEMENT_1
        if(!scoreAchievement1Achieved && player.getScoreValue()>= SCORE_ACHIEVEMENT_1){
            scoreAchievement1Achieved=true;
            player.getGameScreen().getGame().getSharedPreferences().saveAchievement("1000 Points Achievement Achieved", true);
            new DisplayToast(gainScoreAchievement1).execute();
        }

        //if the player HASN'T got the coin achievement 1 AND the player's number of coins is greater than or equal to the COINS_ACHIEVEMENT_FOR_COLLECTING_TEN_COIN
        if(!coinAchievement1Achieved && player.getNumOfCoins()>=COINS_ACHIEVEMENT_FOR_COLLECTING_TEN_COIN){
            coinAchievement1Achieved=true;
            player.getGameScreen().getGame().getSharedPreferences().saveAchievement("Ten Coins Achievement Achievement", true);
            new DisplayToast(gainTenthCoin).execute();
        }

        //if the player HASN'T got the kill achievement 1 AND the player's number of enemies killed is greater than or equal to the KILLS_ACHIEVEMENT_FOR_SHOOTING_TEN_ENEMY
        if(!killsAchievement1Achieved && player.getEnemiesKilled()>=KILLS_ACHIEVEMENT_FOR_SHOOTING_TEN_ENEMY){
            killsAchievement1Achieved=true;
            player.getGameScreen().getGame().getSharedPreferences().saveAchievement("Ten Enemies Killed Achievement", true);
            new DisplayToast(gainTenthEnemy).execute();
        }

        //if the player HASN'T got the score achievement 2 AND the player's score value is greater than or equal to the SCORE_ACHIEVEMENT_2
        if(!scoreAchievement2Achieved && player.getScoreValue()>=SCORE_ACHIEVEMENT_2){
            scoreAchievement2Achieved=true;
            player.getGameScreen().getGame().getSharedPreferences().saveAchievement("2000 Points Achievement Achieved", true);
            new DisplayToast(gainScoreAchievement2).execute();
        }

        //if the player HASN'T got the coin achievement 2 AND the player's number of coins is greater than or equal to the COINS_ACHIEVEMENT_FOR_COLLECTING_FIFTY_COIN
        if(!coinAchievement2Achieved && player.getNumOfCoins()>=COINS_ACHIEVEMENT_FOR_COLLECTING_FIFTY_COIN){
            coinAchievement2Achieved=true;
            player.getGameScreen().getGame().getSharedPreferences().saveAchievement("Fifty Coin Achievement Achieved", true);
            new DisplayToast(gainFiftyCoin).execute();
        }

        //if the player HASN'T got the kill achievement 2 AND the player's number of enemies killed is greater than or equal to the KILLS_ACHIEVEMENT_FOR_SHOOTING_TWENTY_ENEMY
        if(!killsAchievement2Achieved && player.getEnemiesKilled()>=KILLS_ACHIEVEMENT_FOR_SHOOTING_TWENTY_ENEMY){
            killsAchievement2Achieved=true;
            player.getGameScreen().getGame().getSharedPreferences().saveAchievement("Fifty Kills Achievement Achieved", true);
            new DisplayToast(gainTwentyEnemy).execute();
        }

        //if the player HASN'T got the score achievement 3 AND the player's score value is greater than or equal to the SCORE_ACHIEVEMENT_3
        if(!scoreAchievement3Achieved && player.getScoreValue()>=SCORE_ACHIEVEMENT_3){
            scoreAchievement3Achieved=true;
            player.getGameScreen().getGame().getSharedPreferences().saveAchievement("3000 Points Achievement Achieved", true);
            new DisplayToast(gainScoreAchievement3).execute();
        }
    }

    // /////////////////////////////////////////////////////////////////////////
    // Constructor
    // /////////////////////////////////////////////////////////////////////////

    //get achievements states(T/F)
    public boolean getCoinAchievementAchieved(){
        return coinAchievementAchieved;
    }
    public boolean getKillsAchievementAchieved(){
        return  killsAchievementAchieved;
    }
    public boolean getScoreAchievement1Achieved(){
        return scoreAchievement1Achieved;
    }
    public boolean getCoinAchievement1Achieved(){
        return coinAchievement1Achieved;
    }
    public boolean getEnemyAchievement1Achieved(){return killsAchievement1Achieved;}
    public boolean getScoreAchievement2Achieved(){return scoreAchievement2Achieved;}
    public boolean getCoinAchievement2Achieved(){return coinAchievement2Achieved;}
    public boolean getKillsAchievement2Achieved(){return killsAchievement2Achieved;}
    public boolean getScoreAchievement3Achieved(){return scoreAchievement3Achieved;}

    //set achievements
    public void setCoinAchievementAchieved(boolean coinAchievementAchieved){this.coinAchievementAchieved = coinAchievementAchieved;}
    public void setKillsAchievementAchieved(boolean killsAchievementAchieved){this.killsAchievementAchieved = killsAchievementAchieved;}
    public void setScoreAchievement1Achieved(boolean scoreAchievement1Achieved){this.scoreAchievement1Achieved = scoreAchievement1Achieved;}
    public void setCoinAchievement1Achieved(boolean coinAchievement1Achieved){this.coinAchievement1Achieved = coinAchievement1Achieved;}
    public void setKillsAchievement1Achieved(boolean killsAchievement1Achieved) {this.killsAchievement1Achieved = killsAchievement1Achieved;}
    public void setScoreAchievement2Achieved(boolean scoreAchievement2Achieved) {this.scoreAchievement2Achieved = scoreAchievement2Achieved;}
    public void setCoinAchievement2Achieved(boolean coinAchievement2Achieved) {this.coinAchievement2Achieved = coinAchievement2Achieved;}
    public void setKillsAchievement2Achieved(boolean killsAchievement2Achieved) {this.killsAchievement2Achieved = killsAchievement2Achieved;}
    public void setScoreAchievement3Achieved(boolean scoreAchievement3Achieved) {this.scoreAchievement3Achieved = scoreAchievement3Achieved;}

    //getting the achievement strings
    public String getGainFirstCoin(){return gainFirstCoin;}
    public String getTenthCoinAchievement(){return gainTenthCoin;}
    public String getGainFirstEnemy(){return gainFirstEnemy;}
    public String getGainFirstAchievement(){return gainScoreAchievement1;}
    public String getGainTenthEnemyAchievement(){return gainTenthEnemy;}
    public String getGainScoreAchievement2Achievement(){return gainScoreAchievement2;}
    public String getGainFiftyCoinAchievement(){return gainFiftyCoin;}
    public String getGainTwentyEnemyAchievement(){return gainTwentyEnemy;}
    public String getGainScoreAchievement3Achievement(){return gainScoreAchievement3;}
}
