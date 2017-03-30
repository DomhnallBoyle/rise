package rise.myapplication.World.GameObjects;

import android.graphics.Bitmap;
import java.util.ArrayList;
import java.util.Random;

import rise.myapplication.Util.ElapsedTime;
import rise.myapplication.Util.Scale;
import rise.myapplication.World.GameScreen;
import rise.myapplication.Util.CollisionDetector;


/**
 * Created by 40126424 on 15/12/2015.
 */
public class Enemy extends Sprite {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    public enum enemyType{MOVING,STILL}
    public enum movement{LEFT,RIGHT}
    private static final double ENEMY_WIDTH = 15;
    private static final double ENEMY_HEIGHT = 15;
    private final enemyType enemyType;
    private movement movement;
    private boolean isGone;

    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////

    public Enemy(float startX, float startY, enemyType enemyType, GameScreen gameScreen)
    {
        super(startX, startY, Scale.getX(ENEMY_WIDTH), Scale.getY(ENEMY_HEIGHT), gameScreen);
        setBitmap(findRightImage(enemyType));
        this.enemyType=enemyType;
        this.movement = getMovement();
        this.isGone = false;
    }

    //depending on the enemy type, it will set the appropriate image
    private Bitmap findRightImage(enemyType enemyType) {
        Bitmap blank = null;
        switch(enemyType){
            case MOVING:
                blank=getmGameScreen().getGame().getAssetManager().getBitmap("thing");
                break;
            case STILL:
                blank=getmGameScreen().getGame().getAssetManager().getBitmap("RocketWarrior");
                break;
            default:
                break;
        }
        return blank;
    }

    //enemy update method
    public void update(ElapsedTime elapsedTime, Player player, ArrayList<Projectile> friendlyProjectiles, ArrayList<Projectile> enemyProjectiles)
    {
        super.update(elapsedTime);

        //depending on the type of enemy, update its x position
        switch(this.getEnemies())
        {
            case MOVING:
                if (movement == movement.LEFT)
                {
                    getPosition().x-=2;
                }
                else
                {
                    getPosition().x+=2;
                }
                //if the platform goes further than the screenwidth, move it left
                if (getBound().getX()+getBound().getHalfWidth() >= getmGameScreen().getGame().getScreenWidth())
                {
                    movement = movement.LEFT;
                }
                //if the platform goes less than the screenwidth, move it right
                if (getBound().getX()-getBound().getHalfWidth() <= 0)
                {
                    movement = movement.RIGHT;
                }
                break;
            default:
                break;
        }

        //if the players top bound is greater than 42% of the screen, move the players y position downwards
        if (player.getBound().getTop() >= Scale.getY(42))
        {
            getPosition().y-=10;
        }

        //checking to see if projectiles fired from the player have hit an enemy
        checkAndResolveCollisionsFriendly(friendlyProjectiles, player);

        //checking to see if projectiles fired from enemy have hit a player
        checkAndResolveCollisionsEnemyProjectiles(enemyProjectiles, player);
    }

    //check for collisions between enemies and player when the player shoots FRIENDLY projectiles
    private void checkAndResolveCollisionsFriendly(ArrayList<Projectile> playerProjectiles, Player player)
    {
        CollisionDetector.CollisionType collisionType;
        for (Projectile projectile : playerProjectiles) {
            //determine a collision type from a static method in CollisionDetector
            collisionType = CollisionDetector.determineAndResolveCollision(this, projectile);
            //if top, left, right or bottom, explode the enemy, increase the player's score value and set the projectile to gone
            switch (collisionType) {
                case Top:
                case Left:
                case Right:
                case Bottom:
                    if(!isGone){
                        player.setEnemiesKilled(player.getEnemiesKilled() + 1);
                    }
                    this.explode();
                    player.setScoreValue(player.getScoreValue()+100);
                    projectile.setIsGone(true);
                    break;
                default:
                    break;
            }
        }
    }

    //check for collisions between enemies and players when the enemy fires Projectiles at the player
    private void checkAndResolveCollisionsEnemyProjectiles(ArrayList<Projectile> enemyProjectiles, Player player)
    {
        CollisionDetector.CollisionType collisionType;
        for (Projectile projectile : enemyProjectiles) {
            //determine a collision type from a static method in CollisionDetector
            collisionType = CollisionDetector.determineAndResolveCollision(projectile, player);
            //if top, left, right or bottom, explode the enemy, increase the player's score value and set the projectile to gone
            switch (collisionType) {
                case Top:
                case Left:
                case Right:
                case Bottom:
                    player.setHasCollided(true);
                    break;
                default:
                    break;
            }
        }
    }

    //method to explode the enemies and set isGone to true
    public void explode()
    {
        getmGameScreen().getGame().getAssetManager().getSound("Explosion").play();
        this.isGone = true;
    }

    //method to determine the movement of the enemy by using a random generation
    private movement getMovement()
    {
        Random rand = new Random();
        movement movement = null;
        int i = rand.nextInt(100);
        //if the random integer is an even number, set to right. If odd, set to left
        if (i%2==0)
        {
            //assert movement != null;
            movement = movement.RIGHT;
        }
        else
        {
            //assert movement != null;
            movement = movement.LEFT;
        }
        return movement;
    }


    // /////////////////////////////////////////////////////////////////////////
    // Getters & Setters
    // /////////////////////////////////////////////////////////////////////////

    public enemyType getEnemies(){
        return enemyType;
    }
    public void setX(float x){
        getPosition().x = x;
    }
    public void setY(float y){
        getPosition().y = y;
    }
    public void setIsGone(boolean isGone){
        this.isGone = isGone;
    }
    public boolean isGone()
    {
        return isGone;
    }
}
