package rise.myapplication.World.GameObjects;


import java.util.ArrayList;

import rise.myapplication.Util.ElapsedTime;
import rise.myapplication.World.GameScreen;
import rise.myapplication.Util.CollisionDetector;

/**
 * Created by 40124186 on 15/12/2015.
 */
public class Projectile extends Sprite {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    private float startX, startY;
    private double dirX, dirY, dirLength;
    private boolean isGone = false;
    public enum ProjectileType {FRIENDLY, ENEMY}
    private final ProjectileType projectileType;

    // /////////////////////////////////////////////////////////////////////////
    // Constructor
    // /////////////////////////////////////////////////////////////////////////

    public Projectile(float startX, float startY, float destinationX, float destinationY, ProjectileType projectileType, GameScreen gameScreen)
    {
        super(startX, startY, 50.0f, 50.0f, gameScreen.getGame().getAssetManager().getBitmap("Bullet"), gameScreen);
        this.startX = startX;
        this.startY = startY;
        setPosition(startX, startY);
        this.projectileType = projectileType;
        //determine which firing method to use based on projectile type
        if (projectileType == ProjectileType.FRIENDLY)
        {
            playerFire(destinationX, destinationY);
        }
        else
        {
            setBitmap(gameScreen.getGame().getAssetManager().getBitmap("EnemyBullet"));
            enemyFire(destinationX, destinationY);
        }
    }

    //method to reset enemy projectiles when they've been added to the arraylist of removedProjectiles
    public void setEnemyValues(float startX, float startY, float destinationX, float destinationY){
        this.startX = startX;
        this.startY = startY;
        setPosition(startX, startY);
        enemyFire(destinationX, destinationY);
    }

    //method to reset player projectiles when they've been added to the arraylist of removedProjectiles
    public void setPlayerValues(float startX, float startY, float destinationX, float destinationY){
        this.startX = startX;
        this.startY = startY;
        setPosition(startX, startY);
        playerFire(destinationX, destinationY);
    }

    //method for updating the projectiles
    public void update(ElapsedTime elapsedTime, ArrayList<Projectile> enemyProjectiles)
    {
        super.update(elapsedTime);
        getPosition().x += dirX * elapsedTime.stepTime;
        getPosition().y += dirY * elapsedTime.stepTime;
        //if the projectile type is FRIENDLY, check collisions with enemy projectiles
        if (projectileType == ProjectileType.FRIENDLY)
        {
            checkForAndResolveCollisionsWithProjectiles(enemyProjectiles);
        }
    }

    //method for player fire
    private void playerFire(float touchX, float touchY)
    {
        dirX = touchX - startX;
        dirY = touchY - startY;
        velocity.x = touchX - startX;
        velocity.y = startY - touchY;
        velocity.normalise();
        velocity.multiply(750);
        dirLength = Math.sqrt(dirX*dirX + dirY*dirY);
        dirX=dirX/dirLength;
        dirY=dirY/dirLength;
    }

    //method for enemy fire
    private void enemyFire(float playerX, float playerY)
    {
        dirX = playerX - startX;
        dirY = playerY - startY;
        velocity.x = playerX - startX;
        velocity.y = playerY - startY;
        velocity.normalise();
        velocity.multiply(500);
        dirLength = Math.sqrt(dirX*dirX + dirY*dirY);
        dirX=dirX/dirLength;
        dirY=dirY/dirLength;
    }

    //method to check if FRIENDLY projectiles collide with ENEMY projectiles
    private void checkForAndResolveCollisionsWithProjectiles(ArrayList<Projectile> enemyProjectiles) {
        CollisionDetector.CollisionType collisionType;
        for (Projectile projectile : enemyProjectiles) {
            collisionType = CollisionDetector.determineAndResolveCollision(this, projectile);
            switch (collisionType) {
                case Top:
                case Left:
                case Right:
                case Bottom:
                    projectile.isGone = true;
                    isGone = true;
                    break;
                default:
                    break;
            }
        }
    }

    // /////////////////////////////////////////////////////////////////////////
    // Getters & Setters
    // /////////////////////////////////////////////////////////////////////////

    public boolean isGone(){
        return isGone;
    }
    public void setIsGone(boolean isGone){
        this.isGone = isGone;
    }
}