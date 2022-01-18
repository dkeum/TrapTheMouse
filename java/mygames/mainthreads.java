package mygames;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class mainthreads extends Thread {

    private final SurfaceHolder surfaceHolder;
    private final GameViews gameView;
    private boolean running;
    public static Canvas canvas;




    public mainthreads(SurfaceHolder surfaceHolder, GameViews gameView) {
        super();
        this.surfaceHolder = surfaceHolder;
        this.gameView = gameView;
    }

    //  @Override
    public void run() {
        while (running) {
            canvas = null;

            try {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized(surfaceHolder) {
                    this.gameView.update();
                    this.gameView.draw(canvas);
                }
            } catch (Exception e) {} finally {
                if (canvas != null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    public void setRunning(boolean isRunning) {
        running = isRunning;
    }


}





