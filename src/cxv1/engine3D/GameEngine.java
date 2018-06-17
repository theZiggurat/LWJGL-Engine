package cxv1.engine3D;

import cxv1.engine3D.input.MouseInput;
import cxv1.engine3D.util.Timer;
import cxv1.engine3D.util.Window;
import game.TutGameLogic;

public class GameEngine implements Runnable {

    public static final int TARGET_FPS = 120;
    private float CURRENT_FPS;

    private final Window window;
    private final Thread gameLoopThread;
    private final Timer timer;
    private final TutGameLogic logic_mgr;
    private final MouseInput mouseInput;


    public GameEngine(String title, int width, int height,
                      boolean vsync, TutGameLogic logic_mgr) throws Exception{
        gameLoopThread = new Thread(this, "GAME_0");
        window = new Window(title, width, height, vsync);
        this.logic_mgr = logic_mgr;
        timer = new Timer();
        mouseInput = new MouseInput();
    }

    @Override
    public void run(){
        try{
            init();
            loop();
        } catch(Exception e){
            e.printStackTrace();
        } finally {
            cleanup();
        }
    }

    protected void init() throws Exception{
        window.init();
        timer.init();
        mouseInput.init(window);
        logic_mgr.init(window);
    }

    public void start(){
        gameLoopThread.start();
    }

    protected void loop(){

        float frameLength = 1 / TARGET_FPS;
        double lastUpdate = timer.getTime();

        int frameCounter = 0;
        float timeTracker = (float) timer.getTime();
        float toOne = 0;

        while(!window.shouldClose()){

            if(toOne>=1){
                toOne -= 1;
                CURRENT_FPS = frameCounter;
                frameCounter = 0;
                System.out.println("FPS: " + CURRENT_FPS +"  TIME: "+logic_mgr.getState().getRelativeSunTimer());
            }

            timer.mark();
            input();
            update(timer.getTime() - lastUpdate);
            lastUpdate = timer.getTime();
            render();

            while( timer.getTimeToNext(frameLength) - timer.getTime() > -.001 &&
                   timer.getTimeToNext(frameLength) - timer.getTime() < .001){
                try{ Thread.sleep(1);}
                catch(InterruptedException e){}
            }
            toOne += timer.getTime() - timeTracker;
            timeTracker = (float) timer.getTime();
            frameCounter++;
        }

    }

    protected void input(){
        mouseInput.input();
        logic_mgr.input(window, mouseInput);
    }

    protected void update(double interval){
        logic_mgr.update(interval, mouseInput);
    }

    protected void render(){
        logic_mgr.render(window, CURRENT_FPS);
        window.update();
    }

    void cleanup(){
        logic_mgr.cleanup();
    }
}
