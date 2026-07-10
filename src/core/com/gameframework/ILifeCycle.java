package core.com.gameframework;

public interface ILifeCycle {
    public void onCreate();
    public void onUpdate(float deltaTime);
    public void onDestroy();
}
