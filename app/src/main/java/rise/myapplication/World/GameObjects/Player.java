package rise.myapplication.World.GameObjects;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;

import java.util.ArrayList;

import rise.myapplication.Util.Achievements;
import rise.myapplication.UI.Animation;
import rise.myapplication.Util.ElapsedTime;
import rise.myapplication.Engine.Graphics.IGraphics2D;
import rise.myapplication.Engine.Input.AccelerometerHandler;
import rise.myapplication.Game.MainActivity;
import rise.myapplication.Util.Scale;
import rise.myapplication.Util.Score;
import rise.myapplication.World.GameScreen;
import rise.myapplication.Screens.OptionsScreen;
import rise.myapplication.Util.CollisionDetector;
import rise.myapplication.World.LayerViewport;
import rise.myapplication.World.ScreenViewport;

/**
 * Created by 40126424 on 10/12/2015.
 */
public class Player extends Sprite {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Strength of gravity to apply along the y-axis
     */
    private final GameScreen gameScreen;
    private final AccelerometerHandler accelHandler = new AccelerometerHandler(MainActivity.getContext());
    private final Achievements achievements;
    private boolean AccelerometerToggle = false;
    private boolean IsSecondPlayer = false;
    private float GRAVITY = -800.0f;
    private final Animation magnetAnimation;
    private final Animation springAnimation;

    /**
     * Acceleration with which the player can move along
     * the x-axis
     */
    private final float RUN_ACCELERATION = 1500.0f;

    /**
     * Maximum velocity of the player along the x-axis
     */
    private final float MAX_X_VELOCITY = 2000.0f;

    /**
     * Scale factor that is applied to the x-velocity when
     * the player is not moving left or right
     */
    private final float RUN_DECAY = 0.95f;

    /**
     * Instantaneous velocity with which the player jumps up
     */
    private float JUMP_VELOCITY = 1000.0f;

    /**
     * Scale factor that is used to turn the x-velocity into
     * an angular velocity to give the visual appearance
     * that the sphere is rotating as the player moves.
     */
    private float ANGULAR_VELOCITY_SCALE = 1.5f;

    private static final int NUMBER_OF_ATTRIBUTES = 6;

    private float time = 0;
    private int numOfCoins = 0;
    private int enemiesKilled = 0;
    private boolean hasCollided = false;
    private boolean hasBounced = false;

    //powerup states and strengths
    private boolean hasSprings = false;
    private int springJumpsUsed = 0;
    private int springJumpsAllowed = 0;

    private boolean hasRocket = false;
    private float rocketStartTime = 0;
    private float rocketAllowedTime = 0;

    private boolean hasShield = false;
    private float shieldStartTime = 0;
    private float shieldAllowedTime = 0;

    private boolean hasMultiplier = false;
    private int coinMultiplier = 0;
    private float multiplierStartTime = 0;
    private float multiplierAllowedTime = 0;

    private boolean hasMagnet = false;
    private float magnetStartTime = 0;
    private float magnetAllowedTime = 0;

    private int numOfLifes = 0;

    //player's attribute levels
    private int[] attributeLevels;
    //1 - shield, 2 - rocket, 3 - scoreMultiplier, 4 - magnet, 5 - springs

    private float overflowY = 0;
    private final Score score;
    private String name;
    Paint playerPaint = new Paint();

    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Create the player's sphere
     *
     * @param startX
     *            x location of the sphere
     * @param startY
     *            y location of the sphere
     * @param gameScreen
     *            Gamescreen to which sphere belongs
     */

    public Player(float startX, float startY, String name, GameScreen gameScreen) {
        super(startX, startY, 200.0f, 200.0f, gameScreen.getGame().getAssetManager().getBitmap("BlueEyesLeft"), gameScreen);

        //depending on the static character int in the options screen, set the appropriate bitmap image
        switch(OptionsScreen.character)
        {
            case 0: setBitmap(gameScreen.getGame().getAssetManager().getBitmap("BlueEyesLeft")); break;
            case 1: setBitmap(gameScreen.getGame().getAssetManager().getBitmap("DarkMagicianLeft")); break;
            case 2: setBitmap(gameScreen.getGame().getAssetManager().getBitmap("TimeWizard")); break;
            default: setBitmap(gameScreen.getGame().getAssetManager().getBitmap("BlueEyesLeft")); break;
        }

        //setting up achievements
        achievements = new Achievements();

        //setting the gamescreen
        this.gameScreen=gameScreen;

        //setting number of coins
        this.numOfCoins = 0;

        //setting the player's attributes
        attributeLevels = new int[NUMBER_OF_ATTRIBUTES];
        for (int i=0; i<attributeLevels.length; i++)
        {
            attributeLevels[i] = 0;
        }
        increaseAttributes();

        //setting up the score
        score = new Score(0);

        //setting the name
        this.name = name;

        //setting up the player's animations
        magnetAnimation= new Animation(gameScreen.getGame().getAssetManager().getBitmap("magnetCoinAnimation"),9);
        springAnimation = new Animation(gameScreen.getGame().getAssetManager().getBitmap("springAnimation"),6);
    }


    // /////////////////////////////////////////////////////////////////////////
    // Methods
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Update the player
     *
     * @param elapsedTime
     *            Elapsed time information
     * @param moveLeft
     *            True if the move left control is active
     * @param moveRight
     *            True if the move right control is active
     * //@param jumpUp
     *            True if the jump up control is active
     * @param platforms
     *            Array of platforms in the world
     */

    //player's update method for the directional arrows
    public void update(ElapsedTime elapsedTime, boolean moveLeft, boolean moveRight, ArrayList<Platform> platforms, ArrayList<Enemy> enemies, ArrayList<Item> collectibles) {

        //sets the time
        time = (float) elapsedTime.totalTime;

        //updates the animations
        magnetAnimation.update(elapsedTime);
        springAnimation.update(elapsedTime);

        //check if rocket is finished
        if(hasRocket && elapsedTime.totalTime - rocketStartTime >= rocketAllowedTime){
            GRAVITY = -800.0f;
            hasRocket = false;
        }

        //check if magnet is finished
        if(hasShield && elapsedTime.totalTime - shieldStartTime > shieldAllowedTime){
            hasShield = false;
            clearPaint();
        }

        //check if multiplier is finished
        if(hasMultiplier && elapsedTime.totalTime - multiplierStartTime > multiplierAllowedTime){
            hasMultiplier = false;
        }

        //check if magnet is finished
        if(hasMagnet && elapsedTime.totalTime - magnetStartTime > magnetAllowedTime){
            hasMagnet = false;
        }

        // Apply gravity to the y-axis acceleration
        acceleration.y = GRAVITY;

        //cap height halfway up screen
        if (this.getBound().getY() >= Scale.getY(50))
        {
            overflowY = this.getBound().getY() - Scale.getY(50);
            this.setPosition(this.getPosition().x, Scale.getY(50));
        }else{
            overflowY = 0;
        }

        // Depending upon the left and right movement touch controls
        // set an appropriate x-acceleration. If the user does not
        // want to move left or right, then the x-acceleration is zero
        // and the velocity decays towards zero.
        if (moveLeft && !moveRight) {
            acceleration.x = -RUN_ACCELERATION;
            switch(OptionsScreen.character)
            {
                case 0: setBitmap(gameScreen.getGame().getAssetManager().getBitmap("BlueEyesLeft")); break;
                case 1: setBitmap(gameScreen.getGame().getAssetManager().getBitmap("DarkMagicianLeft")); break;
                case 2: setBitmap(gameScreen.getGame().getAssetManager().getBitmap("TimeWizard")); break;
                default: setBitmap(gameScreen.getGame().getAssetManager().getBitmap("BlueEyesLeft")); break;
            }

        } else if (moveRight && !moveLeft) {
            acceleration.x = RUN_ACCELERATION;
            switch(OptionsScreen.character)
            {
                case 0: setBitmap(gameScreen.getGame().getAssetManager().getBitmap("BlueEyesRight")); break;
                case 1: setBitmap(gameScreen.getGame().getAssetManager().getBitmap("DarkMagicianRight")); break;
                case 2: setBitmap(gameScreen.getGame().getAssetManager().getBitmap("TimeWizard")); break;
                default: setBitmap(gameScreen.getGame().getAssetManager().getBitmap("BlueEyesRight")); break;
            }
        } else {
            acceleration.x = 0.0f;
            velocity.x *= RUN_DECAY;
        }

        // If the user wants to jump up then providing an immediate
        // boost to the y velocity.
        if (velocity.y == 0.0f) {
            velocity.y = JUMP_VELOCITY;
            hasBounced = true;
        }

        //check if achievements have been completed
        achievements.checkAchievements(this);

        // Call the sprite's update method to apply the defined
        // accelerations and velocities to provide a new position
        // and orientation.
        super.update(elapsedTime);

        // The player's sphere is constrained by a maximum x-velocity,
        // but not a y-velocity. Make sure we have not exceeded this.
        if (Math.abs(velocity.x) > MAX_X_VELOCITY)
            velocity.x = Math.signum(velocity.x) * MAX_X_VELOCITY;

        // Check that our new position has not collided by one of the
        // defined platforms. If so, then removing any overlap and
        // ensure a valid velocity.
        checkForAndResolveCollisions(platforms);
        checkForAndResolveCollisionsWithEnemy(enemies);
        checkForCollisions(collectibles);

        //update the score
        score.update(elapsedTime);
    }

    public void setPlayerBitmap()
    {
        switch(OptionsScreen.character)
        {
            case 0: setBitmap(gameScreen.getGame().getAssetManager().getBitmap("BlueEyesRight")); break;
            case 1: setBitmap(gameScreen.getGame().getAssetManager().getBitmap("DarkMagicianRight")); break;
            case 2: setBitmap(gameScreen.getGame().getAssetManager().getBitmap("TimeWizard")); break;
            default: setBitmap(gameScreen.getGame().getAssetManager().getBitmap("BlueEyesRight")); break;
        }
    }
    public Paint getPaint(){return playerPaint;}

    public void setPaint(Paint playerPaint){
        this.playerPaint=playerPaint;
    }
    public void clearPaint(){
        playerPaint.reset();
    }

    public void update(ElapsedTime elapsedTime, ArrayList<Platform> platforms, ArrayList<Enemy> enemies, ArrayList<Item> collectibles) {
        time = (float) elapsedTime.totalTime;
        score.update(elapsedTime);

        //check if rocket is finished
        if(hasRocket && elapsedTime.totalTime - rocketStartTime >= rocketAllowedTime){
            GRAVITY = -800.0f;
            hasRocket = false;
        }

        //check if shield is finished
        if(hasShield && elapsedTime.totalTime - shieldStartTime > shieldAllowedTime){
            hasShield = false;
            clearPaint();
        }

        //check if multiplier is finished
        if(hasMultiplier && elapsedTime.totalTime - multiplierStartTime > multiplierAllowedTime){
            hasMultiplier = false;
        }

        //check if magnet is finished
        if(hasMagnet && elapsedTime.totalTime - magnetStartTime > magnetAllowedTime){
            hasMagnet = false;
        }

        // Apply gravity to the y-axis acceleration
        acceleration.y = GRAVITY;

        if (this.getBound().getY() >= Scale.getY(50))
        {
            overflowY = this.getBound().getY() - Scale.getY(50);
            this.setPosition(this.getPosition().x, Scale.getY(50));
        }else{
            overflowY = 0;
        }

        // Depending upon the left and right movement touch controls
        // set an appropriate x-acceleration. If the user does not
        // want to move left or right, then the x-acceleration is zero
        // and the velocity decays towards zero.

        velocity.x = -350*accelHandler.getAccelX();
        if (velocity.x <= 0 ){
            switch(OptionsScreen.character)
            {
                case 0: setBitmap(gameScreen.getGame().getAssetManager().getBitmap("BlueEyesLeft")); break;
                case 1: setBitmap(gameScreen.getGame().getAssetManager().getBitmap("DarkMagicianLeft")); break;
                case 2: setBitmap(gameScreen.getGame().getAssetManager().getBitmap("TimeWizard")); break;
                default: setBitmap(gameScreen.getGame().getAssetManager().getBitmap("BlueEyesLeft")); break;
            }
        }
        else if (velocity.x > 0){
            switch(OptionsScreen.character)
                {
                    case 0: setBitmap(gameScreen.getGame().getAssetManager().getBitmap("BlueEyesRight")); break;
                    case 1: setBitmap(gameScreen.getGame().getAssetManager().getBitmap("DarkMagicianRight")); break;
                    case 2: setBitmap(gameScreen.getGame().getAssetManager().getBitmap("TimeWizard")); break;
                    default: setBitmap(gameScreen.getGame().getAssetManager().getBitmap("BlueEyesRight")); break;
                }
            }


        // If the user wants to jump up then providing an immediate
        // boost to the y velocity.

        if (velocity.y == 0.0f) {
            velocity.y = JUMP_VELOCITY;
            hasBounced = true;
        }

        achievements.checkAchievements(this);

        // Call the sprite's update method to apply the defined
        // accelerations and velocities to provide a new position
        // and orientation.
        super.update(elapsedTime);

        // The player's sphere is constrained by a maximum x-velocity,
        // but not a y-velocity. Make sure we have not exceeded this.
        if (Math.abs(velocity.x) > MAX_X_VELOCITY)
            velocity.x = Math.signum(velocity.x) * MAX_X_VELOCITY;

        // Check that our new position has not collided by one of the
        // defined platforms. If so, then removing any overlap and
        // ensure a valid velocity.
        checkForAndResolveCollisions(platforms);
        checkForAndResolveCollisionsWithEnemy(enemies);
        checkForCollisions(collectibles);
    }


    //this update method is used for the player in the shop
    public void update(ElapsedTime elapsedTime, Platform platform) {
        acceleration.x = 0;
        acceleration.y = GRAVITY;

        if (velocity.y == 0.0f) {
            velocity.y = JUMP_VELOCITY;
        }

        super.update(elapsedTime);

        checkForAndResolveCollisions(platform);
        achievements.checkAchievements(this);
    }

    @Override
    public void draw(IGraphics2D graphics2D, LayerViewport layerViewport, ScreenViewport screenViewport, Paint paint)
    {
        super.draw(graphics2D, layerViewport, screenViewport, paint);
        if (hasMagnet) {
            magnetAnimation.draw(graphics2D, getDrawScreenRect().left - Scale.getX(5), getDrawScreenRect().top - Scale.getY(5), getDrawScreenRect().right + Scale.getX(5), getDrawScreenRect().bottom + Scale.getY(5), paint);
            if (magnetAnimation.getImageCount() == magnetAnimation.getFrameCount()) {
                magnetAnimation.setImageCount(0);
            }
        }
        if (hasSprings)
        {
            springAnimation.draw(graphics2D, getDrawScreenRect().left + Scale.getX(5), getDrawScreenRect().bottom, getDrawScreenRect().right - Scale.getX(5), getDrawScreenRect().bottom + Scale.getX(5), paint);
            if (springAnimation.getImageCount() == springAnimation.getFrameCount()) {
                springAnimation.setImageCount(0);
            }
        }
    }

    private void checkForAndResolveCollisions(Platform platform) {

        CollisionDetector.CollisionType collisionType;
        if(velocity.y < 0){
            collisionType = CollisionDetector.determineAndResolvePlatformCollision(this, platform);

            switch (collisionType) {
                case Top:
                    velocity.y = 0.0f;
                    break;

                default:
                    break;
            }
        }
    }


    /**
     * Check for and then resolve any collision between the sphere and the
     * platforms.
     *
     * @param platforms
     *            Array of platforms to test for collision against
     */
    private void checkForAndResolveCollisions(ArrayList<Platform> platforms) {

       CollisionDetector.CollisionType collisionType;
        if(velocity.y < 0){
            for (Platform platform : platforms) {
                collisionType = CollisionDetector.determineAndResolvePlatformCollision(this, platform);

                switch (collisionType) {
                    case Top:
                        if(hasSprings){
                            getGameScreen().getGame().getAssetManager().getSound("SpringJump").play();
                            springJumpsUsed++;
                            if(springJumpsUsed >= springJumpsAllowed){
                                hasSprings = false;
                                JUMP_VELOCITY = 1000.0f;
                            }
                        }
                        else
                        {
                            getGameScreen().getGame().getAssetManager().getSound("NormalJump").play();
                        }
                        switch(platform.getPlatformType())
                        {
                            case EXPLODING: platform.explode(); break;
                            default: break;
                        }
                        velocity.y = 0.0f;
                        break;

                    default:
                        break;
                }
            }
        }
    }


    public void checkForCollisions(ArrayList<Item> collectibles) {

        CollisionDetector.CollisionType collisionType;
        for (Item item : collectibles) {
            if (!item.IsGone()) {

                //get type of collision from static method
                collisionType = CollisionDetector.determineAndResolvePlatformCollision(this, item);

                //if the player hits the collectible from angle
                //based on the collectible type, different actions will occur
                switch (collisionType) {
                    case Top:
                    case Bottom:
                    case Left:
                    case Right:
                        switch(item.getType()){
                            case COIN:
                                if(hasMultiplier)
                                {
                                    numOfCoins += coinMultiplier; //if the player has the coin multiplier, increase the no. of coins by coinMultiplier
                                    setScoreValue(getScoreValue() + coinMultiplier);
                                }
                                else
                                {
                                    numOfCoins++; //increase coins by 1
                                }
                                getGameScreen().getGame().getAssetManager().getSound("CoinCollect").play();
                                break;
                            case SPRINGS:
                                hasSprings = true;
                                springJumpsUsed = 0; //set springJumpsUsed to 0
                                JUMP_VELOCITY = 1500;
                                springAnimation.play(2,true);
                                break;
                            case ROCKET:
                                hasRocket = true;
                                rocketStartTime = time;
                                velocity.y = JUMP_VELOCITY;
                                GRAVITY = 0;
                                break;
                            case SHIELD:
                                hasShield = true;
                                shieldStartTime = time;
                                playerPaint.setColorFilter(new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY));
                                break;
                            case MULTIPLIER:
                                hasMultiplier = true;
                                multiplierStartTime = time; break;
                            case MAGNET:
                                hasMagnet = true;
                                magnetStartTime = time;
                                magnetAnimation.play(2, true);
                                break;
                            default:
                                break;
                        }
                        item.setIsGone(true);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    //checks to see if player collided with enemy
    private void checkForAndResolveCollisionsWithEnemy(ArrayList<Enemy> enemies) {

        CollisionDetector.CollisionType collisionType;
        if(!hasShield) {
            for (Enemy badguy : enemies) {
                //gets type of collision
                collisionType = CollisionDetector.determineAndResolveCollision(this, badguy);

                //if player hits top of enemy, enemy explodes and player gets 100 points
                //if player hits the enemy anywhere else, the player dies
                switch (collisionType) {
                    case Top:
                        badguy.explode();
                        velocity.y = 0.0f;
                        setEnemiesKilled(enemiesKilled+1);
                        score.setScore(getScoreValue() + 100);
                        break;
                    case Left:
                    case Right:
                    case Bottom:
                        hasCollided = true;
                        break;
                    default:
                        break;
                }
            }
        }
    }

    // /////////////////////////////////////////////////////////////////////////
    // Getters & Setters
    // /////////////////////////////////////////////////////////////////////////

    //reset all attributes for new game
    public void reset()
    {
        this.getPosition().x = Scale.getX(50);
        this.getPosition().y = Scale.getY(35);
        JUMP_VELOCITY = 1000.0f;
        hasCollided = false;
        this.velocity.x = 0.0f;
        this.velocity.y = 0.0f;
        hasMagnet = false;
        hasMultiplier = false;
        hasShield = false;
        hasSprings = false;
        hasRocket = false;
        magnetAnimation.stop();
        springAnimation.stop();
        clearPaint();
    }

    //accelerometer
    public void setAccelerometerToggle(boolean accel){ AccelerometerToggle = accel; }
    public boolean getAccelerometerToggle(){ return AccelerometerToggle; }

    //second player
    public void setIsSecondPlayer(boolean secondPlayer){ IsSecondPlayer = secondPlayer; }
    public boolean getIsSecondPlayer(){ return IsSecondPlayer; }

    //num of coins
    public int getNumOfCoins(){return numOfCoins;}
    public void setNumOfCoins(int numOfCoins){this.numOfCoins = numOfCoins;}

    //enemies killed
    public void setEnemiesKilled(int enemiesKilled){this.enemiesKilled = enemiesKilled;}
    public int getEnemiesKilled(){return enemiesKilled;}

    //hasBounced
    public boolean getHasBounced(){return hasBounced;}
    public void setHasBounced(boolean hasBounced){this.hasBounced = hasBounced;}

    //hasCollided
    public boolean getHasCollided(){return hasCollided;}
    public void setHasCollided(boolean hasCollided){this.hasCollided = hasCollided;}

    //powerups
    public boolean getHasSpring(){
        return  hasSprings;
    }

    public boolean getHasMagnet(){return hasMagnet;}
    public float getMagnetStartTime(){return magnetStartTime;}
    public float getMagnetAllowedTime(){return magnetAllowedTime;}

    public void increaseAttributes()
    {
        shieldAllowedTime = 5 + getAttributeLevel(0);
        rocketAllowedTime = 3 + getAttributeLevel(1);
        multiplierAllowedTime = 5 + getAttributeLevel(2);
        coinMultiplier = 5 + getAttributeLevel(2);
        magnetAllowedTime = 5 + getAttributeLevel(3);
        springJumpsAllowed = 3 + getAttributeLevel(4);
    }

    //increae player's attributes
    public void increaseLevelAt(int shopItem)
    {
        switch(shopItem)
        {
            case 0: ++attributeLevels[0]; break; //increment shield allowed time
            case 1: ++attributeLevels[1]; break; //increment rocket allowed time
            case 2: ++attributeLevels[2]; break; //increment multiplier allowed time
            case 3: ++attributeLevels[3]; break; //increment magnet allowed time
            case 4: ++attributeLevels[4]; break; //increment spring jumps
            case 5: ++attributeLevels[5]; break; //increment number of lives
        }
    }

    public int getAttributeLevel(int shopItem)
    {
        int level = 0;
        switch(shopItem)
        {
            case 0: level = attributeLevels[0]; break; //get shield allowed time
            case 1: level = attributeLevels[1]; break; //get rocket allowed time
            case 2: level = attributeLevels[2]; break; //get multiplier allowed time
            case 3: level = attributeLevels[3]; break; //get magnet allowed time
            case 4: level = attributeLevels[4]; break; //get spring jumps level
            case 5: level = attributeLevels[5]; break; //get number of lives
        }
        return level;
    }

    public void setAttributeLevel(int index, int value){
        attributeLevels[index] = value;
    }

    public int[] getAttributeLevels()
    {
        return attributeLevels;
    }

    public void setAttributeLevels(int[] attributeLevels)
    {
        this.attributeLevels = attributeLevels;
    }

    //overflowY
    public float getOverflowY(){return overflowY;}

    //score
    public Score getScore(){
        return score;
    }
    public int getScoreValue(){
        return score.getScore();
    }
    public void setScoreValue(int scoreValue)
    {
        this.score.setScore(scoreValue);
    }
    public void resetScore(){
        score.setScore(0);
    }

    //name
    public void setName(String name){this.name = name;}
    public String getName(){return name;}

    //validation for making sure the name is not above an upperbound
    public void checkName(int upperBound)
    {
        if (name.length() > upperBound)
        {
            name = name.substring(0, upperBound);
        }
    }

    //Animations
    public Animation getMagnetAnimation(){
        return magnetAnimation;
    }
    public Animation getSpringAnimation(){
        return springAnimation;
    }

    //achievements
    public Achievements getAchievements(){
     return achievements;
 }

    //GameScreen
    public GameScreen getGameScreen(){return getmGameScreen();}

    public int getNumOfLifes(){return numOfLifes;}
    public void increaseLife(){numOfLifes++;}
    public void decreaseLife(){numOfLifes--;}
}