package simplicity.Model.Resource;

import lombok.Getter;
import simplicity.Model.GameModel;
import simplicity.Model.Listeners.AnimationTickListener;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

public class Animation {

    private static final ArrayList<AnimationTickListener> animationTickListeners = new ArrayList<>();
    @Getter
    private final Point position;
    @Getter
    private final int speed;
    private final ArrayList<Image> list;
    private final long startTime;
    private int currentImage;
    @Getter
    private boolean started;
    @Getter
    private boolean ended;

    public Animation(Point position, int speed, Image... images) {
        this.position = position;
        this.speed = speed;
        this.list = new ArrayList<>(Arrays.asList(images));
        this.currentImage = -1;
        this.startTime = System.currentTimeMillis();
        this.started = false;
        this.ended = false;
    }

    public static void addAnimationTickListener(AnimationTickListener l) {
        animationTickListeners.add(l);
    }

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

    public ArrayList<Image> getList() {
        return (ArrayList<Image>) this.list.clone();
    }

    public Image getCurrentImage() {
        return (this.currentImage < 0 || this.currentImage > list.size() - 1) ? null : this.list.get(this.currentImage);
    }

    public int getCurrentImageIndex() {
        return this.currentImage;
    }

    private Image next() {
        this.currentImage = (this.currentImage >= list.size() - 1) ? 0 : this.currentImage + 1;
        return this.getCurrentImage();
    }

    public void start() {
        this.currentImage = 0;
        scheduleNext();
    }

    public void stop() {
        this.ended = true;
        for (AnimationTickListener l : animationTickListeners) l.onAnimationTick();
    }

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
