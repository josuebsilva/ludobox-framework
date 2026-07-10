package core.com.ludobox.gameobjects;

public class Object {
    private boolean pendingDestroy;
    public void destroy() {
        this.pendingDestroy = true;
    }

    public boolean isPendingDestroy() {
        return pendingDestroy;
    }
}
