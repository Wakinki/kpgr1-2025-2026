package state;

import controller.Controller2D;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public abstract class DrawingState {
    protected final Controller2D ctrl;

    public DrawingState(Controller2D ctrl) {
        this.ctrl = ctrl;
    }

    public void onMousePressed(MouseEvent e){};
    public void onMouseReleased(MouseEvent e){};
    public void onMouseMoved(MouseEvent e){};
    public void onMouseDragged(MouseEvent e){};
    public void onKeyPressed(KeyEvent e){};
    public void onKeyReleased(KeyEvent e){};

    public void drawPreview(){};
    public void onEnterState(){};   // vyčistí preview, resetuje stav
    public void onExitState(){};    // dodělá co je třeba
}