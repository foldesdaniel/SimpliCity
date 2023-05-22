package simplicity.Model.Resource;

import lombok.Getter;
import simplicity.Model.GameModel;
import simplicity.Model.Listeners.AnimationTickListener;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Class that stores a series of Images that
 * should be animated, and animation settings
 */
public class Animation {

    private static final ArrayList<AnimationTickListener> animationTickListeners = new ArrayList<>();
    @Getter
    private final Point position;
    @Getter
    private final int speed;
    private final ArrayList<Image> list;
    private int currentImage;
    @Getter
    private boolean ended;

    /**
     * Constructs an Animation
     *
     * @param position the coordinates it should be played at
     * @param speed    the time it takes to reach the next Image
     * @param images   array of Images
     */
    public Animation(Point position, int speed, Image... images) {
        this.position = position;
        this.speed = speed;
        this.list = new ArrayList<>(Arrays.asList(images));
        this.currentImage = -1;
        this.ended = false;
    }

    public static void addAnimationTickListener(AnimationTickListener l) {
        animationTickListeners.add(l);
    }

    /**
     * Creates a default fire animation
     *
     * @param position the coordinates it should be played at
     * @return the animation
     */
    public static Animation createFireAnim(Point position) {
        return new Animation(position, 200,
                GameModel.FIRE_ANIM_1,
                GameModel.FIRE_ANIM_2,
                GameModel.FIRE_ANIM_3,
                GameModel.FIRE_ANIM_4,
                GameModel.FIRE_ANIM_5,
                GameModel.FIRE_ANIM_6,
                GameModel.FIRE_ANIM_7,
                GameModel.FIRE_ANIM_8
        );
    }

    /**
     * @return the current Image based on the animation state
     */
    public Image getCurrentImage() {
        return (this.currentImage < 0 || this.currentImage > list.size() - 1) ? null : this.list.get(this.currentImage);
    }

    /**
     * Changes image to the next
     */
    private void next() {
        this.currentImage = (this.currentImage >= list.size() - 1) ? 0 : this.currentImage + 1;
    }

    /**
     * Starts the animation
     */
    public void start() {
        this.currentImage = 0;
        scheduleNext();
    }

    /**
     * Stops the animation
     */
    public void stop() {
        this.ended = true;
        for (AnimationTickListener l : animationTickListeners) l.onAnimationTick();
    }

    /**
     * Times the animation to change state after the given time
     */
    private void scheduleNext() {
        Timer animTimer = new Timer();
        animTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!ended) {
                    next();
                    scheduleNext();
                }
            }
        }, this.speed);
        for (AnimationTickListener l : animationTickListeners) l.onAnimationTick();
    }

}
