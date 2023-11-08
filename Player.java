package com.mygdx.game.GameOne;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Player extends Attackable {
    float speed;
    boolean flip = false;
    boolean dead = false;
    SpriteSheet sheet;

    SpriteSheet idleSheet;
    SpriteSheet runSheet;
    SpriteSheet attack1Sheet;
    SpriteSheet attack2Sheet;
    SpriteSheet attack3Sheet;
    Vector2 vel = new Vector2(0,0);
    int attack = 0;

    public Player(Vector2 pos) {
        super(pos,null);

        idleSheet = new SpriteSheet(new Texture("gameOne/player_idle.png"),1,2);
        idleSheet.setPlay(0, 1, 0.15f, true);
        runSheet = new SpriteSheet(new Texture("gameOne/player_run.png"),1,8);
        runSheet.setPlay(0, 7, 0.08f, true);

        attack1Sheet = new SpriteSheet(new Texture("gameOne/player_attack.png"),1,15);
        attack1Sheet.setPlay(0, 4, 0.08f, false);
        attack2Sheet = new SpriteSheet(new Texture("gameOne/player_attack.png"),1,15);
        attack2Sheet.setPlay(5, 9, 0.08f, false);
        attack3Sheet = new SpriteSheet(new Texture("gameOne/player_attack.png"),1,15);
        attack3Sheet.setPlay(10, 14, 0.08f, false);

        sheet = idleSheet;
        speed = 7;

    }

    void flipAllSheet(boolean flipX){
        idleSheet.flip(flipX);
        runSheet.flip(flipX);
        attack1Sheet.flip(flipX);
        attack2Sheet.flip(flipX);
        attack3Sheet.flip(flipX);
    }


    @Override
    public void update() {
        // TODO Auto-generated method stub
        super.update();

        if(dead) {
            return;
        }
//        if(pos.x < 0 || pos.x > Utils.get().WIDTH || pos.y <0 || pos.y > Utils.get().HEIGHT) {
//            dead = true;
//        }

        if(attack > 0){
            if(sheet == attack1Sheet && sheet.current >= 4){
                attack1Sheet.setPlay(0, 4, 0.08f, false);
                if(attack >= 2){
                    sheet = attack2Sheet;
                    moveAttack();
                    Utils.get().mSound.play("sword2",1);

                }
                else {
                    attack = 0;
                    sheet = idleSheet;
                }
            }
            else if(sheet == attack2Sheet && sheet.current >= 9){
                attack2Sheet.setPlay(5, 9, 0.08f, false);
                if(attack >= 3){
                    sheet = attack3Sheet;
                    Utils.get().mSound.play("sword3",1);

                    moveAttack();
                }
                else {
                    attack = 0;
                    sheet = idleSheet;
                }
            }
            else if(sheet == attack3Sheet && sheet.current >= 14){
                attack3Sheet.setPlay(10, 14, 0.08f, false);
                attack = 0;
                sheet = idleSheet;
            }
        }
        else {
            if(vel.x != 0 || vel.y != 0){
                sheet = runSheet;
            }
            else {
                sheet = idleSheet;
            }
        }

        sheet.play();
        pos.add(vel);
    }

    void moveAttack(){
        int move = 50;
        if(flip){
            pos.x -= move;
        }
        else {
            pos.x += move;
        }
    }

    public SpriteSheet getAttackSheet(){
        if(sheet == attack1Sheet || sheet == attack2Sheet || sheet == attack3Sheet){
            return sheet;
        }
        return null;
    }

    @Override
    public void draw(Batch batch) {
        TextureRegion t = sheet.getCurrentFrame();
        if((sheet == attack1Sheet || sheet == attack2Sheet || sheet == attack3Sheet) && flip){
            super.drawImg(new Vector2(pos.x - 150,pos.y),batch,t);
            return;
        }
        super.drawImg(new Vector2(pos.x,pos.y),batch,t);

    }

    public Rect getRect(){
        TextureRegion t = sheet.getCurrentFrame();
        if((sheet == attack1Sheet || sheet == attack2Sheet || sheet == attack3Sheet) && flip){
            return new Rect(pos.x - 150,pos.y,t.getRegionWidth(),t.getRegionHeight());
        }
        return new Rect(pos.x,pos.y,t.getRegionWidth(),t.getRegionHeight());
    }

    public boolean isDead() {
        return dead;
    }


    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public void attack(){
        if(attack == 0){
            Utils.get().mSound.play("sword1",1);

            sheet = attack1Sheet;
            attack++;
        }
        else if(attack == 1 ){
            attack++;
        }
        else if(attack <= 2 ){
            attack++;
        }
    }

    public void onKeyUp(int keycode) {
        switch(keycode) {
            case 29:
                if (vel.x == -speed) {
                    vel.x = 0;
                }
                break;
            case 32:
                if (vel.x == speed) {
                    vel.x = 0;
                }
                break;
            case Input.Keys.W:
                if (vel.y == -speed) {
                    vel.y = 0;
                }
                break;
            case Input.Keys.S:
                if (vel.y == speed) {
                    vel.y = 0;
                }
        }

    }

    public void onKeyDown(int keycode) {
        switch(keycode) {
            case Input.Keys.A: //press a
                if(!flip) {
                    flipAllSheet(true);
                    flip = true;
                }
                vel.x = -speed;
                break;
            case Input.Keys.D: //press d
                if(flip) {
                    flipAllSheet(true);
                    flip = false;
                }
                vel.x = speed;
                break;
            case Input.Keys.W:
                vel.y = -speed;
                break;
            case Input.Keys.S:
                vel.y = speed;
                break;
            case 62: // space
                break;
            case 111: // esc
                Utils.get().gameOver = true;
                break;
        }
    }

}
