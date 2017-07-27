package ru.erked.dt.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import ru.erked.dt.StartDT;
import ru.erked.dt.systems.DTButton;
import ru.erked.dt.systems.DTGui;
import ru.erked.dt.utilities.Obfuscation;
import ru.erked.dt.utilities.Subject;
import ru.erked.dt.utilities.Weapon;

public class Shop implements Screen {

    private StartDT game;
    private Sprite background, moneyBag;
    private Sprite[] hero;
    private int iter = 0, weaponIter = 0;
    private float time = 0f;
    private Stage stage;
    private Subject checked;
    private DTButton toPrison, buy, select, next, prev;
    private boolean nextToPrison = false;
    private boolean[] weaponShow;
    private Obfuscation obf;
    private Subject[] posters, spears, bows, sticks;
    private DTGui weaponGui;

    public Shop(StartDT game){
        this.game = game;
    }

    private void buttonInit(){
        toPrison = new DTButton(
                game,
                0.025f * stage.getWidth(),
                0.05f * stage.getWidth(),
                0.3f * stage.getWidth(),
                game.MFontS.getFont(),
                game.textSystem.get("to_prison"),
                1,
                "to_prison"
        );
        toPrison.get().addListener(new ClickListener(){
            @Override
            public void clicked (InputEvent event, float x, float y) {
                if(!obf.isActive() && !weaponGui.isDisplayed()) {
                    StartDT.clickSound.play();
                    nextToPrison = true;
                } else {
                    toPrison.get().setChecked(false);
                }
            }
        });
        posters = new Subject[4];
        posters[0] = new Subject(
                game.atlas.createSprite("poster", 1),
                -0.01f * stage.getWidth(),
                0.25f * stage.getHeight(),
                0.275f * stage.getHeight(),
                0.275f * stage.getHeight()
        );
        posters[0].addListener(new ClickListener(){
            @Override
            public void clicked (InputEvent event, float x, float y) {
                if(!weaponGui.isDisplayed() && !posters[1].hasActions() && !posters[2].hasActions() && !posters[3].hasActions()) {
                    StartDT.clickSound.play();
                    posters[0].addAction(Actions.moveTo(posters[0].getX() - posters[0].getWidth(), posters[0].getY(), 0.75f));
                    weaponShow[0] = true;
                }
            }
        });
        posters[1] = new Subject(
                game.atlas.createSprite("poster", 2),
                0.05f * stage.getWidth(),
                0.525f * stage.getHeight(),
                0.275f * stage.getHeight(),
                0.275f * stage.getHeight()
        );
        posters[1].addListener(new ClickListener(){
            @Override
            public void clicked (InputEvent event, float x, float y) {
                if(!weaponGui.isDisplayed() && !posters[0].hasActions() && !posters[2].hasActions() && !posters[3].hasActions()) {
                    StartDT.clickSound.play();
                    posters[1].addAction(Actions.moveTo(posters[1].getX() - posters[1].getWidth(), posters[1].getY(), 0.75f));
                    weaponShow[1] = true;
                }
            }
        });
        posters[2] = new Subject(
                game.atlas.createSprite("poster", 3),
                0.515f * stage.getWidth(),
                0.55f * stage.getHeight(),
                0.3f * stage.getHeight(),
                0.3f * stage.getHeight()
        );
        posters[2].addListener(new ClickListener(){
            @Override
            public void clicked (InputEvent event, float x, float y) {
                if(!weaponGui.isDisplayed() && !posters[0].hasActions() && !posters[1].hasActions() && !posters[3].hasActions()) {
                    StartDT.clickSound.play();
                    posters[2].addAction(Actions.moveTo(posters[2].getX() + posters[2].getWidth(), posters[2].getY(), 0.75f));
                    weaponShow[2] = true;
                }
            }
        });
        posters[3] = new Subject(
                game.atlas.createSprite("poster", 4),
                0.55f * stage.getWidth(),
                0.275f * stage.getHeight(),
                0.275f * stage.getHeight(),
                0.275f * stage.getHeight()
        );
        posters[3].addListener(new ClickListener(){
            @Override
            public void clicked (InputEvent event, float x, float y) {
                if(!weaponGui.isDisplayed() && !posters[0].hasActions() && !posters[1].hasActions() && !posters[2].hasActions()) {
                    StartDT.clickSound.play();
                    posters[3].addAction(Actions.moveTo(posters[3].getX() + posters[3].getWidth(), posters[3].getY(), 0.75f));
                    weaponShow[3] = true;
                }
            }
        });
        for(int i = 0; i < 4; ++i){ stage.addActor(posters[i]); }
        buy = new DTButton(
                game,
                0.35f * stage.getWidth(),
                0.135f * stage.getHeight(),
                0.3f * stage.getWidth(),
                game.MFontS.getFont(),
                game.textSystem.get("buy"),
                1,
                "buy"
        );
        buy.get().addListener(new ClickListener(){
            @Override
            public void clicked (InputEvent event, float x, float y) {
                if(!obf.isActive() && weaponGui.isDisplayed()) {
                    StartDT.clickSound.play();
                    buy.get().setChecked(false);
                    if (checked.getX() + 10 >= 0.67f * stage.getWidth()) {

                        if(weaponShow[0]){
                            if(StartDT.player.getXpLevel() >= StartDT.weapons[(weaponIter + 2) * 3].getPlayerXp()) {
                                if(StartDT.player.getMoney() >= StartDT.weapons[(weaponIter + 2) * 3].getPrice()) {
                                    StartDT.player.spendMoney(StartDT.weapons[(weaponIter + 2) * 3].getPrice());
                                    StartDT.player.setWeapon(StartDT.weapons[(weaponIter + 2) * 3]);
                                    for(Weapon weapon : StartDT.weapons) weapon.setSelected(false);
                                    StartDT.weapons[(weaponIter + 2) * 3].setOwned(true);
                                    StartDT.weapons[(weaponIter + 2) * 3].setSelected(true);
                                    select.get().setText(game.textSystem.get("checked"));
                                    select.get().setChecked(true);
                                    select.get().setVisible(true);
                                    buy.get().setVisible(false);
                                }
                            }
                        }
                        if(weaponShow[1]){
                            if(StartDT.player.getXpLevel() >= StartDT.weapons[(weaponIter + 2) * 3 + 1].getPlayerXp()) {
                                if(StartDT.player.getMoney() >= StartDT.weapons[(weaponIter + 2) * 3 + 1].getPrice()) {
                                    StartDT.player.spendMoney(StartDT.weapons[(weaponIter + 2) * 3 + 1].getPrice());
                                    StartDT.player.setWeapon(StartDT.weapons[(weaponIter + 2) * 3 + 1]);
                                    for(Weapon weapon : StartDT.weapons) weapon.setSelected(false);
                                    StartDT.weapons[(weaponIter + 2) * 3 + 1].setOwned(true);
                                    StartDT.weapons[(weaponIter + 2) * 3 + 1].setSelected(true);
                                    select.get().setText(game.textSystem.get("checked"));
                                    select.get().setChecked(true);
                                    select.get().setVisible(true);
                                    buy.get().setVisible(false);
                                }
                            }
                        }
                        if(weaponShow[2]){
                            if(StartDT.player.getXpLevel() >= StartDT.weapons[(weaponIter + 2) * 3 + 2].getPlayerXp()) {
                                if(StartDT.player.getMoney() >= StartDT.weapons[(weaponIter + 2) * 3 + 2].getPrice()) {
                                    StartDT.player.spendMoney(StartDT.weapons[(weaponIter + 2) * 3 + 2].getPrice());
                                    StartDT.player.setWeapon(StartDT.weapons[(weaponIter + 2) * 3 + 2]);
                                    for(Weapon weapon : StartDT.weapons) weapon.setSelected(false);
                                    StartDT.weapons[(weaponIter + 2) * 3 + 2].setOwned(true);
                                    StartDT.weapons[(weaponIter + 2) * 3 + 2].setSelected(true);
                                    select.get().setText(game.textSystem.get("checked"));
                                    select.get().setChecked(true);
                                    select.get().setVisible(true);
                                    buy.get().setVisible(false);
                                }
                            }
                        }
                        if(weaponShow[3]){

                        }

                    } else if(checked.getX() + 10 >= 0.39f * stage.getWidth()){

                        if(weaponShow[0]){
                            if(StartDT.player.getXpLevel() >= StartDT.weapons[(weaponIter + 1) * 3].getPlayerXp()) {
                                if(StartDT.player.getMoney() >= StartDT.weapons[(weaponIter + 1) * 3].getPrice()) {
                                    StartDT.player.spendMoney(StartDT.weapons[(weaponIter + 1) * 3].getPrice());
                                    StartDT.player.setWeapon(StartDT.weapons[(weaponIter + 1) * 3]);
                                    for(Weapon weapon : StartDT.weapons) weapon.setSelected(false);
                                    StartDT.weapons[(weaponIter + 1) * 3].setOwned(true);
                                    StartDT.weapons[(weaponIter + 1) * 3].setSelected(true);
                                    select.get().setText(game.textSystem.get("checked"));
                                    select.get().setChecked(true);
                                    select.get().setVisible(true);
                                    buy.get().setVisible(false);
                                }
                            }
                        }
                        if(weaponShow[1]){
                            if(StartDT.player.getXpLevel() >= StartDT.weapons[(weaponIter + 1) * 3 + 1].getPlayerXp()) {
                                if(StartDT.player.getMoney() >= StartDT.weapons[(weaponIter + 1) * 3 + 1].getPrice()) {
                                    StartDT.player.spendMoney(StartDT.weapons[(weaponIter + 1) * 3 + 1].getPrice());
                                    StartDT.player.setWeapon(StartDT.weapons[(weaponIter + 1) * 3 + 1]);
                                    for(Weapon weapon : StartDT.weapons) weapon.setSelected(false);
                                    StartDT.weapons[(weaponIter + 1) * 3 + 1].setOwned(true);
                                    StartDT.weapons[(weaponIter + 1) * 3 + 1].setSelected(true);
                                    select.get().setText(game.textSystem.get("checked"));
                                    select.get().setChecked(true);
                                    select.get().setVisible(true);
                                    buy.get().setVisible(false);
                                }
                            }
                        }
                        if(weaponShow[2]){
                            if(StartDT.player.getXpLevel() >= StartDT.weapons[(weaponIter + 1) * 3 + 2].getPlayerXp()) {
                                if(StartDT.player.getMoney() >= StartDT.weapons[(weaponIter + 1) * 3 + 2].getPrice()) {
                                    StartDT.player.spendMoney(StartDT.weapons[(weaponIter + 1) * 3 + 2].getPrice());
                                    StartDT.player.setWeapon(StartDT.weapons[(weaponIter + 1) * 3 + 2]);
                                    for(Weapon weapon : StartDT.weapons) weapon.setSelected(false);
                                    StartDT.weapons[(weaponIter + 1) * 3 + 2].setOwned(true);
                                    StartDT.weapons[(weaponIter + 1) * 3 + 2].setSelected(true);
                                    select.get().setText(game.textSystem.get("checked"));
                                    select.get().setChecked(true);
                                    select.get().setVisible(true);
                                    buy.get().setVisible(false);
                                }
                            }
                        }
                        if(weaponShow[3]){

                        }

                    } else {

                        if(weaponShow[0]){
                            if(StartDT.player.getXpLevel() >= StartDT.weapons[weaponIter * 3].getPlayerXp()) {
                                if(StartDT.player.getMoney() >= StartDT.weapons[weaponIter * 3].getPrice()) {
                                    StartDT.player.spendMoney(StartDT.weapons[weaponIter * 3].getPrice());
                                    StartDT.player.setWeapon(StartDT.weapons[weaponIter * 3]);
                                    for(Weapon weapon : StartDT.weapons) weapon.setSelected(false);
                                    StartDT.weapons[weaponIter * 3].setOwned(true);
                                    StartDT.weapons[weaponIter * 3].setSelected(true);
                                    select.get().setText(game.textSystem.get("checked"));
                                    select.get().setChecked(true);
                                    select.get().setVisible(true);
                                    buy.get().setVisible(false);
                                }
                            }
                        }
                        if(weaponShow[1]){
                            if(StartDT.player.getXpLevel() >= StartDT.weapons[weaponIter * 3 + 1].getPlayerXp()) {
                                if(StartDT.player.getMoney() >= StartDT.weapons[weaponIter * 3 + 1].getPrice()) {
                                    StartDT.player.spendMoney(StartDT.weapons[weaponIter * 3 + 1].getPrice());
                                    StartDT.player.setWeapon(StartDT.weapons[weaponIter * 3 + 1]);
                                    for(Weapon weapon : StartDT.weapons) weapon.setSelected(false);
                                    StartDT.weapons[weaponIter * 3 + 1].setOwned(true);
                                    StartDT.weapons[weaponIter * 3 + 1].setSelected(true);
                                    select.get().setText(game.textSystem.get("checked"));
                                    select.get().setChecked(true);
                                    select.get().setVisible(true);
                                    buy.get().setVisible(false);
                                }
                            }
                        }
                        if(weaponShow[2]){
                            if(StartDT.player.getXpLevel() >= StartDT.weapons[weaponIter * 3 + 2].getPlayerXp()) {
                                if(StartDT.player.getMoney() >= StartDT.weapons[weaponIter * 3 + 2].getPrice()) {
                                    StartDT.player.spendMoney(StartDT.weapons[weaponIter * 3 + 2].getPrice());
                                    StartDT.player.setWeapon(StartDT.weapons[weaponIter * 3 + 2]);
                                    for(Weapon weapon : StartDT.weapons) weapon.setSelected(false);
                                    StartDT.weapons[weaponIter * 3 + 2].setOwned(true);
                                    StartDT.weapons[weaponIter * 3 + 2].setSelected(true);
                                    select.get().setText(game.textSystem.get("checked"));
                                    select.get().setChecked(true);
                                    select.get().setVisible(true);
                                    buy.get().setVisible(false);
                                }
                            }
                        }
                        if(weaponShow[3]){

                        }

                    }
                } else {
                    buy.get().setChecked(false);
                }
            }
        });
        buy.get().setVisible(false);
        select = new DTButton(
                game,
                0.35f * stage.getWidth(),
                0.135f * stage.getHeight(),
                0.3f * stage.getWidth(),
                game.MFontS.getFont(),
                game.textSystem.get("select"),
                1,
                "select"
        );
        select.get().addListener(new ClickListener(){
            @Override
            public void clicked (InputEvent event, float x, float y) {
                if(!obf.isActive() && weaponGui.isDisplayed()) {
                    StartDT.clickSound.play();
                    select.get().setChecked(false);
                    if (checked.getX() + 10 >= 0.67f * stage.getWidth()) {

                        if(weaponShow[0]){
                            for(Weapon weapon : StartDT.weapons) weapon.setSelected(false);
                            StartDT.weapons[(weaponIter + 2) * 3].setSelected(true);
                            StartDT.player.setWeapon(StartDT.weapons[(weaponIter + 2) * 3]);
                            select.get().setText(game.textSystem.get("checked"));
                            select.get().setChecked(true);
                        }
                        if(weaponShow[1]){
                            for(Weapon weapon : StartDT.weapons) weapon.setSelected(false);
                            StartDT.weapons[(weaponIter + 2) * 3 + 1].setSelected(true);
                            StartDT.player.setWeapon(StartDT.weapons[(weaponIter + 2) * 3 + 1]);
                            select.get().setText(game.textSystem.get("checked"));
                            select.get().setChecked(true);
                        }
                        if(weaponShow[2]){
                            for(Weapon weapon : StartDT.weapons) weapon.setSelected(false);
                            StartDT.weapons[(weaponIter + 2) * 3 + 2].setSelected(true);
                            StartDT.player.setWeapon(StartDT.weapons[(weaponIter + 2) * 3 + 2]);
                            select.get().setText(game.textSystem.get("checked"));
                            select.get().setChecked(true);
                        }
                        if(weaponShow[3]){

                        }

                    } else if(checked.getX() + 10 >= 0.39f * stage.getWidth()){

                        if(weaponShow[0]){
                            for(Weapon weapon : StartDT.weapons) weapon.setSelected(false);
                            StartDT.weapons[(weaponIter + 1) * 3].setSelected(true);
                            StartDT.player.setWeapon(StartDT.weapons[(weaponIter + 1) * 3]);
                            select.get().setText(game.textSystem.get("checked"));
                            select.get().setChecked(true);
                        }
                        if(weaponShow[1]){
                            for(Weapon weapon : StartDT.weapons) weapon.setSelected(false);
                            StartDT.weapons[(weaponIter + 1) * 3 + 1].setSelected(true);
                            StartDT.player.setWeapon(StartDT.weapons[(weaponIter + 1) * 3 + 1]);
                            select.get().setText(game.textSystem.get("checked"));
                            select.get().setChecked(true);
                        }
                        if(weaponShow[2]){
                            for(Weapon weapon : StartDT.weapons) weapon.setSelected(false);
                            StartDT.weapons[(weaponIter + 1) * 3 + 2].setSelected(true);
                            StartDT.player.setWeapon(StartDT.weapons[(weaponIter + 1) * 3 + 2]);
                            select.get().setText(game.textSystem.get("checked"));
                            select.get().setChecked(true);
                        }
                        if(weaponShow[3]){

                        }

                    } else {

                        if(weaponShow[0]){
                            for(Weapon weapon : StartDT.weapons) weapon.setSelected(false);
                            StartDT.weapons[weaponIter * 3].setSelected(true);
                            StartDT.player.setWeapon(StartDT.weapons[weaponIter * 3]);
                            select.get().setText(game.textSystem.get("checked"));
                            select.get().setChecked(true);
                        }
                        if(weaponShow[1]){
                            for(Weapon weapon : StartDT.weapons) weapon.setSelected(false);
                            StartDT.weapons[weaponIter * 3 + 1].setSelected(true);
                            StartDT.player.setWeapon(StartDT.weapons[weaponIter * 3 + 1]);
                            select.get().setText(game.textSystem.get("checked"));
                            select.get().setChecked(true);
                        }
                        if(weaponShow[2]){
                            for(Weapon weapon : StartDT.weapons) weapon.setSelected(false);
                            StartDT.weapons[weaponIter * 3 + 2].setSelected(true);
                            StartDT.player.setWeapon(StartDT.weapons[weaponIter * 3 + 2]);
                            select.get().setText(game.textSystem.get("checked"));
                            select.get().setChecked(true);
                        }
                        if(weaponShow[3]){

                        }

                    }
                } else {
                    select.get().setChecked(false);
                }
            }
        });
        select.get().setVisible(false);
        next = new DTButton(
                game,
                0.75f * stage.getWidth(),
                0.135f * stage.getHeight(),
                0.15f * stage.getWidth(),
                game.LFontS.getFont(),
                ">",
                2,
                "next"
        );
        next.get().addListener(new ClickListener(){
            @Override
            public void clicked (InputEvent event, float x, float y) {
                if(!obf.isActive() && weaponGui.isDisplayed()) {
                    StartDT.clickSound.play();
                    if(weaponShow[0]) {
                        if (weaponIter + 3 != spears.length) weaponIter++;
                    } else if(weaponShow[1]){
                        if (weaponIter + 3 != bows.length) weaponIter++;
                    } else if(weaponShow[2]){
                        if (weaponIter + 3 != sticks.length) weaponIter++;
                    } else if(weaponShow[3]){

                    }
                    next.get().setChecked(false);
                    checked.setVisible(false);
                    select.get().setVisible(false);
                    buy.get().setVisible(false);
                } else {
                    next.get().setChecked(false);
                }
            }
        });
        next.get().setVisible(false);
        prev = new DTButton(
                game,
                0.1f * stage.getWidth(),
                0.135f * stage.getHeight(),
                0.15f * stage.getWidth(),
                game.LFontS.getFont(),
                "<",
                2,
                "prev"
        );
        prev.get().addListener(new ClickListener(){
            @Override
            public void clicked (InputEvent event, float x, float y) {
                if(!obf.isActive() && weaponGui.isDisplayed()) {
                    StartDT.clickSound.play();
                    if(weaponIter > 0) weaponIter--;
                    prev.get().setChecked(false);
                    checked.setVisible(false);
                    select.get().setVisible(false);
                    buy.get().setVisible(false);
                } else {
                    prev.get().setChecked(false);
                }
            }
        });
        prev.get().setVisible(false);
    }
    private void textDraw(){
        if(weaponShow[0]) {
            if (buy.get().isVisible() || select.get().isVisible()) {

                if (checked.getX() + 10 >= 0.67f * stage.getWidth()) {

                    game.MFontS.draw(
                            stage.getBatch(),
                            game.textSystem.get("damage_") + ": " + StartDT.weapons[(weaponIter + 2) * 3].getDamage(),
                            0.1f * stage.getWidth(),
                            0.35f * stage.getHeight()
                    );
                    game.MFontS.draw(
                            stage.getBatch(),
                            game.textSystem.get("delay") + ": " + StartDT.weapons[(weaponIter + 2) * 3].getDelay(),
                            0.1f * stage.getWidth(),
                            0.35f * stage.getHeight() - 1.5f * game.MFontS.getHeight("A")
                    );
                    if(StartDT.player.getXpLevel() >= StartDT.weapons[(weaponIter + 2) * 3].getPlayerXp() && !StartDT.weapons[(weaponIter + 2) * 3].isOwned()) {
                        game.MFontS.setColor(Color.YELLOW);
                        game.MFontS.draw(
                                stage.getBatch(),
                                game.textSystem.get("price") + ": " + StartDT.weapons[(weaponIter + 2) * 3].getPrice(),
                                0.5f * (stage.getWidth() - game.MFontS.getWidth(game.textSystem.get("price") + ": " + StartDT.weapons[(weaponIter + 2) * 3].getPrice())),
                                0.35f * stage.getHeight() + 2.0f * game.MFontS.getHeight("A")
                        );
                        game.MFontS.setColor(Color.WHITE);
                    } else if(!StartDT.weapons[(weaponIter + 2) * 3].isOwned()){
                        game.MFontS.setColor(Color.YELLOW);
                        game.MFontS.draw(
                                stage.getBatch(),
                                game.textSystem.get("level_required") + ": " + StartDT.weapons[(weaponIter + 2) * 3].getPlayerXp(),
                                0.5f * (stage.getWidth() - game.MFontS.getWidth(game.textSystem.get("level_required") + ": " + StartDT.weapons[(weaponIter + 2) * 3].getPlayerXp())),
                                0.35f * stage.getHeight() + 2.0f * game.MFontS.getHeight("A")
                        );
                        game.MFontS.setColor(Color.WHITE);
                    }
                    String text;
                    text = game.textSystem.get("n_spear_" + ((((weaponIter + 2) * 3) / 3) + 1));
                    game.MFontS.draw(
                            stage.getBatch(),
                            text,
                            0.5f * (stage.getWidth() - game.MFontS.getWidth(text)),
                            0.475f * stage.getHeight()
                    );

                } else if (checked.getX() + 10 >= 0.39f * stage.getWidth()) {

                    game.MFontS.draw(
                            stage.getBatch(),
                            game.textSystem.get("damage_") + ": " + StartDT.weapons[(weaponIter + 1) * 3].getDamage(),
                            0.1f * stage.getWidth(),
                            0.35f * stage.getHeight()
                    );
                    game.MFontS.draw(
                            stage.getBatch(),
                            game.textSystem.get("delay") + ": " + StartDT.weapons[(weaponIter + 1) * 3].getDelay(),
                            0.1f * stage.getWidth(),
                            0.35f * stage.getHeight() - 1.5f * game.MFontS.getHeight("A")
                    );
                    if(StartDT.player.getXpLevel() >= StartDT.weapons[(weaponIter + 1) * 3].getPlayerXp() && !StartDT.weapons[(weaponIter + 1) * 3].isOwned()) {
                        game.MFontS.setColor(Color.YELLOW);
                        game.MFontS.draw(
                                stage.getBatch(),
                                game.textSystem.get("price") + ": " + StartDT.weapons[(weaponIter + 1) * 3].getPrice(),
                                0.5f * (stage.getWidth() - game.MFontS.getWidth(game.textSystem.get("price") + ": " + StartDT.weapons[(weaponIter + 1) * 3].getPrice())),
                                0.35f * stage.getHeight() + 2.0f * game.MFontS.getHeight("A")
                        );
                        game.MFontS.setColor(Color.WHITE);
                    } else if(!StartDT.weapons[(weaponIter + 1) * 3].isOwned()){
                        game.MFontS.setColor(Color.YELLOW);
                        game.MFontS.draw(
                                stage.getBatch(),
                                game.textSystem.get("level_required") + ": " + StartDT.weapons[(weaponIter + 1) * 3].getPlayerXp(),
                                0.5f * (stage.getWidth() - game.MFontS.getWidth(game.textSystem.get("level_required") + ": " + StartDT.weapons[(weaponIter + 1) * 3].getPlayerXp())),
                                0.35f * stage.getHeight() + 2.0f * game.MFontS.getHeight("A")
                        );
                        game.MFontS.setColor(Color.WHITE);
                    }
                    String text;
                    text = game.textSystem.get("n_spear_" + ((((weaponIter + 1) * 3) / 3) + 1));
                    game.MFontS.draw(
                            stage.getBatch(),
                            text,
                            0.5f * (stage.getWidth() - game.MFontS.getWidth(text)),
                            0.475f * stage.getHeight()
                    );

                } else {

                    game.MFontS.draw(
                            stage.getBatch(),
                            game.textSystem.get("damage_") + ": " + StartDT.weapons[weaponIter * 3].getDamage(),
                            0.1f * stage.getWidth(),
                            0.35f * stage.getHeight()
                    );
                    game.MFontS.draw(
                            stage.getBatch(),
                            game.textSystem.get("delay") + ": " + StartDT.weapons[weaponIter * 3].getDelay(),
                            0.1f * stage.getWidth(),
                            0.35f * stage.getHeight() - 1.5f * game.MFontS.getHeight("A")
                    );
                    if(StartDT.player.getXpLevel() >= StartDT.weapons[weaponIter * 3].getPlayerXp() && !StartDT.weapons[weaponIter * 3].isOwned()) {
                        game.MFontS.setColor(Color.YELLOW);
                        game.MFontS.draw(
                                stage.getBatch(),
                                game.textSystem.get("price") + ": " + StartDT.weapons[weaponIter * 3].getPrice(),
                                0.5f * (stage.getWidth() - game.MFontS.getWidth(game.textSystem.get("price") + ": " + StartDT.weapons[weaponIter * 3].getPrice())),
                                0.35f * stage.getHeight() + 2.0f * game.MFontS.getHeight("A")
                        );
                        game.MFontS.setColor(Color.WHITE);
                    } else if(!StartDT.weapons[weaponIter * 3].isOwned()){
                        game.MFontS.setColor(Color.YELLOW);
                        game.MFontS.draw(
                                stage.getBatch(),
                                game.textSystem.get("level_required") + ": " + StartDT.weapons[weaponIter * 3].getPlayerXp(),
                                0.5f * (stage.getWidth() - game.MFontS.getWidth(game.textSystem.get("level_required") + ": " + StartDT.weapons[weaponIter * 3].getPlayerXp())),
                                0.35f * stage.getHeight() + 2.0f * game.MFontS.getHeight("A")
                        );
                        game.MFontS.setColor(Color.WHITE);
                    }
                    String text;
                    text = game.textSystem.get("n_spear_" + (((weaponIter * 3) / 3) + 1));
                    game.MFontS.draw(
                            stage.getBatch(),
                            text,
                            0.5f * (stage.getWidth() - game.MFontS.getWidth(text)),
                            0.475f * stage.getHeight()
                    );
                }
            }
        } else if(weaponShow[1]){
            if (buy.get().isVisible() || select.get().isVisible()) {

                if (checked.getX() + 10 >= 0.67f * stage.getWidth()) {

                    game.MFontS.draw(
                            stage.getBatch(),
                            game.textSystem.get("damage_") + ": " + StartDT.weapons[(weaponIter + 2) * 3 + 1].getDamage(),
                            0.1f * stage.getWidth(),
                            0.35f * stage.getHeight()
                    );
                    game.MFontS.draw(
                            stage.getBatch(),
                            game.textSystem.get("delay") + ": " + StartDT.weapons[(weaponIter + 2) * 3 + 1].getDelay(),
                            0.1f * stage.getWidth(),
                            0.35f * stage.getHeight() - 1.5f * game.MFontS.getHeight("A")
                    );
                    if(StartDT.player.getXpLevel() >= StartDT.weapons[(weaponIter + 2) * 3 + 1].getPlayerXp() && !StartDT.weapons[(weaponIter + 2) * 3 + 1].isOwned()) {
                        game.MFontS.setColor(Color.YELLOW);
                        game.MFontS.draw(
                                stage.getBatch(),
                                game.textSystem.get("price") + ": " + StartDT.weapons[(weaponIter + 2) * 3 + 1].getPrice(),
                                0.5f * (stage.getWidth() - game.MFontS.getWidth(game.textSystem.get("price") + ": " + StartDT.weapons[(weaponIter + 2) * 3 + 1].getPrice())),
                                0.35f * stage.getHeight() + 2.0f * game.MFontS.getHeight("A")
                        );
                        game.MFontS.setColor(Color.WHITE);
                    } else if(!StartDT.weapons[(weaponIter + 2) * 3 + 1].isOwned()){
                        game.MFontS.setColor(Color.YELLOW);
                        game.MFontS.draw(
                                stage.getBatch(),
                                game.textSystem.get("level_required") + ": " + StartDT.weapons[(weaponIter + 2) * 3 + 1].getPlayerXp(),
                                0.5f * (stage.getWidth() - game.MFontS.getWidth(game.textSystem.get("level_required") + ": " + StartDT.weapons[(weaponIter + 2) * 3 + 1].getPlayerXp())),
                                0.35f * stage.getHeight() + 2.0f * game.MFontS.getHeight("A")
                        );
                        game.MFontS.setColor(Color.WHITE);
                    }
                    String text;
                    text = game.textSystem.get("n_bow_" + ((((weaponIter + 2) * 3) / 3) + 1));
                    game.MFontS.draw(
                            stage.getBatch(),
                            text,
                            0.5f * (stage.getWidth() - game.MFontS.getWidth(text)),
                            0.475f * stage.getHeight()
                    );

                } else if (checked.getX() + 10 >= 0.39f * stage.getWidth()) {

                    game.MFontS.draw(
                            stage.getBatch(),
                            game.textSystem.get("damage_") + ": " + StartDT.weapons[(weaponIter + 1) * 3 + 1].getDamage(),
                            0.1f * stage.getWidth(),
                            0.35f * stage.getHeight()
                    );
                    game.MFontS.draw(
                            stage.getBatch(),
                            game.textSystem.get("delay") + ": " + StartDT.weapons[(weaponIter + 1) * 3 + 1].getDelay(),
                            0.1f * stage.getWidth(),
                            0.35f * stage.getHeight() - 1.5f * game.MFontS.getHeight("A")
                    );
                    if(StartDT.player.getXpLevel() >= StartDT.weapons[(weaponIter + 1) * 3 + 1].getPlayerXp() && !StartDT.weapons[(weaponIter + 1) * 3 + 1].isOwned()) {
                        game.MFontS.setColor(Color.YELLOW);
                        game.MFontS.draw(
                                stage.getBatch(),
                                game.textSystem.get("price") + ": " + StartDT.weapons[(weaponIter + 1) * 3 + 1].getPrice(),
                                0.5f * (stage.getWidth() - game.MFontS.getWidth(game.textSystem.get("price") + ": " + StartDT.weapons[(weaponIter + 1) * 3 + 1].getPrice())),
                                0.35f * stage.getHeight() + 2.0f * game.MFontS.getHeight("A")
                        );
                        game.MFontS.setColor(Color.WHITE);
                    } else if(!StartDT.weapons[(weaponIter + 1) * 3 + 1].isOwned()){
                        game.MFontS.setColor(Color.YELLOW);
                        game.MFontS.draw(
                                stage.getBatch(),
                                game.textSystem.get("level_required") + ": " + StartDT.weapons[(weaponIter + 1) * 3 + 1].getPlayerXp(),
                                0.5f * (stage.getWidth() - game.MFontS.getWidth(game.textSystem.get("level_required") + ": " + StartDT.weapons[(weaponIter + 1) * 3 + 1].getPlayerXp())),
                                0.35f * stage.getHeight() + 2.0f * game.MFontS.getHeight("A")
                        );
                        game.MFontS.setColor(Color.WHITE);
                    }
                    String text;
                    text = game.textSystem.get("n_bow_" + ((((weaponIter + 1) * 3) / 3) + 1));
                    game.MFontS.draw(
                            stage.getBatch(),
                            text,
                            0.5f * (stage.getWidth() - game.MFontS.getWidth(text)),
                            0.475f * stage.getHeight()
                    );

                } else {

                    game.MFontS.draw(
                            stage.getBatch(),
                            game.textSystem.get("damage_") + ": " + StartDT.weapons[weaponIter * 3 + 1].getDamage(),
                            0.1f * stage.getWidth(),
                            0.35f * stage.getHeight()
                    );
                    game.MFontS.draw(
                            stage.getBatch(),
                            game.textSystem.get("delay") + ": " + StartDT.weapons[weaponIter * 3 + 1].getDelay(),
                            0.1f * stage.getWidth(),
                            0.35f * stage.getHeight() - 1.5f * game.MFontS.getHeight("A")
                    );
                    if(StartDT.player.getXpLevel() >= StartDT.weapons[weaponIter * 3 + 1].getPlayerXp() && !StartDT.weapons[weaponIter * 3 + 1].isOwned()) {
                        game.MFontS.setColor(Color.YELLOW);
                        game.MFontS.draw(
                                stage.getBatch(),
                                game.textSystem.get("price") + ": " + StartDT.weapons[weaponIter * 3 + 1].getPrice(),
                                0.5f * (stage.getWidth() - game.MFontS.getWidth(game.textSystem.get("price") + ": " + StartDT.weapons[weaponIter * 3 + 1].getPrice())),
                                0.35f * stage.getHeight() + 2.0f * game.MFontS.getHeight("A")
                        );
                        game.MFontS.setColor(Color.WHITE);
                    } else if(!StartDT.weapons[weaponIter * 3 + 1].isOwned()){
                        game.MFontS.setColor(Color.YELLOW);
                        game.MFontS.draw(
                                stage.getBatch(),
                                game.textSystem.get("level_required") + ": " + StartDT.weapons[weaponIter * 3 + 1].getPlayerXp(),
                                0.5f * (stage.getWidth() - game.MFontS.getWidth(game.textSystem.get("level_required") + ": " + StartDT.weapons[weaponIter * 3 + 1].getPlayerXp())),
                                0.35f * stage.getHeight() + 2.0f * game.MFontS.getHeight("A")
                        );
                        game.MFontS.setColor(Color.WHITE);
                    }
                    String text;
                    text = game.textSystem.get("n_bow_" + (((weaponIter * 3) / 3) + 1));
                    game.MFontS.draw(
                            stage.getBatch(),
                            text,
                            0.5f * (stage.getWidth() - game.MFontS.getWidth(text)),
                            0.475f * stage.getHeight()
                    );
                }
            }
        } else if(weaponShow[2]){
            if (buy.get().isVisible() || select.get().isVisible()) {

                if (checked.getX() + 10 >= 0.67f * stage.getWidth()) {

                    game.MFontS.draw(
                            stage.getBatch(),
                            game.textSystem.get("damage_") + ": " + StartDT.weapons[(weaponIter + 2) * 3 + 2].getDamage(),
                            0.1f * stage.getWidth(),
                            0.35f * stage.getHeight()
                    );
                    game.MFontS.draw(
                            stage.getBatch(),
                            game.textSystem.get("delay") + ": " + StartDT.weapons[(weaponIter + 2) * 3 + 2].getDelay(),
                            0.1f * stage.getWidth(),
                            0.35f * stage.getHeight() - 1.5f * game.MFontS.getHeight("A")
                    );
                    if(StartDT.player.getXpLevel() >= StartDT.weapons[(weaponIter + 2) * 3 + 2].getPlayerXp() && !StartDT.weapons[(weaponIter + 2) * 3 + 2].isOwned()) {
                        game.MFontS.setColor(Color.YELLOW);
                        game.MFontS.draw(
                                stage.getBatch(),
                                game.textSystem.get("price") + ": " + StartDT.weapons[(weaponIter + 2) * 3 + 2].getPrice(),
                                0.5f * (stage.getWidth() - game.MFontS.getWidth(game.textSystem.get("price") + ": " + StartDT.weapons[(weaponIter + 2) * 3 + 2].getPrice())),
                                0.35f * stage.getHeight() + 2.0f * game.MFontS.getHeight("A")
                        );
                        game.MFontS.setColor(Color.WHITE);
                    } else if(!StartDT.weapons[(weaponIter + 2) * 3 + 2].isOwned()){
                        game.MFontS.setColor(Color.YELLOW);
                        game.MFontS.draw(
                                stage.getBatch(),
                                game.textSystem.get("level_required") + ": " + StartDT.weapons[(weaponIter + 2) * 3 + 2].getPlayerXp(),
                                0.5f * (stage.getWidth() - game.MFontS.getWidth(game.textSystem.get("level_required") + ": " + StartDT.weapons[(weaponIter + 2) * 3 + 2].getPlayerXp())),
                                0.35f * stage.getHeight() + 2.0f * game.MFontS.getHeight("A")
                        );
                        game.MFontS.setColor(Color.WHITE);
                    }
                    String text;
                    text = game.textSystem.get("n_stick_" + ((((weaponIter + 2) * 3) / 3) + 1));
                    game.MFontS.draw(
                            stage.getBatch(),
                            text,
                            0.5f * (stage.getWidth() - game.MFontS.getWidth(text)),
                            0.475f * stage.getHeight()
                    );

                } else if (checked.getX() + 10 >= 0.39f * stage.getWidth()) {

                    game.MFontS.draw(
                            stage.getBatch(),
                            game.textSystem.get("damage_") + ": " + StartDT.weapons[(weaponIter + 1) * 3 + 2].getDamage(),
                            0.1f * stage.getWidth(),
                            0.35f * stage.getHeight()
                    );
                    game.MFontS.draw(
                            stage.getBatch(),
                            game.textSystem.get("delay") + ": " + StartDT.weapons[(weaponIter + 1) * 3 + 2].getDelay(),
                            0.1f * stage.getWidth(),
                            0.35f * stage.getHeight() - 1.5f * game.MFontS.getHeight("A")
                    );
                    if(StartDT.player.getXpLevel() >= StartDT.weapons[(weaponIter + 1) * 3 + 2].getPlayerXp() && !StartDT.weapons[(weaponIter + 1) * 3 + 2].isOwned()) {
                        game.MFontS.setColor(Color.YELLOW);
                        game.MFontS.draw(
                                stage.getBatch(),
                                game.textSystem.get("price") + ": " + StartDT.weapons[(weaponIter + 1) * 3 + 2].getPrice(),
                                0.5f * (stage.getWidth() - game.MFontS.getWidth(game.textSystem.get("price") + ": " + StartDT.weapons[(weaponIter + 1) * 3 + 2].getPrice())),
                                0.35f * stage.getHeight() + 2.0f * game.MFontS.getHeight("A")
                        );
                        game.MFontS.setColor(Color.WHITE);
                    } else if(!StartDT.weapons[(weaponIter + 1) * 3 + 2].isOwned()){
                        game.MFontS.setColor(Color.YELLOW);
                        game.MFontS.draw(
                                stage.getBatch(),
                                game.textSystem.get("level_required") + ": " + StartDT.weapons[(weaponIter + 1) * 3 + 1].getPlayerXp(),
                                0.5f * (stage.getWidth() - game.MFontS.getWidth(game.textSystem.get("level_required") + ": " + StartDT.weapons[(weaponIter + 1) * 3 + 2].getPlayerXp())),
                                0.35f * stage.getHeight() + 2.0f * game.MFontS.getHeight("A")
                        );
                        game.MFontS.setColor(Color.WHITE);
                    }
                    String text;
                    text = game.textSystem.get("n_stick_" + ((((weaponIter + 1) * 3) / 3) + 1));
                    game.MFontS.draw(
                            stage.getBatch(),
                            text,
                            0.5f * (stage.getWidth() - game.MFontS.getWidth(text)),
                            0.475f * stage.getHeight()
                    );

                } else {

                    game.MFontS.draw(
                            stage.getBatch(),
                            game.textSystem.get("damage_") + ": " + StartDT.weapons[weaponIter * 3 + 2].getDamage(),
                            0.1f * stage.getWidth(),
                            0.35f * stage.getHeight()
                    );
                    game.MFontS.draw(
                            stage.getBatch(),
                            game.textSystem.get("delay") + ": " + StartDT.weapons[weaponIter * 3 + 2].getDelay(),
                            0.1f * stage.getWidth(),
                            0.35f * stage.getHeight() - 1.5f * game.MFontS.getHeight("A")
                    );
                    if(StartDT.player.getXpLevel() >= StartDT.weapons[weaponIter * 3 + 2].getPlayerXp() && !StartDT.weapons[weaponIter * 3 + 2].isOwned()) {
                        game.MFontS.setColor(Color.YELLOW);
                        game.MFontS.draw(
                                stage.getBatch(),
                                game.textSystem.get("price") + ": " + StartDT.weapons[weaponIter * 3 + 2].getPrice(),
                                0.5f * (stage.getWidth() - game.MFontS.getWidth(game.textSystem.get("price") + ": " + StartDT.weapons[weaponIter * 3 + 2].getPrice())),
                                0.35f * stage.getHeight() + 2.0f * game.MFontS.getHeight("A")
                        );
                        game.MFontS.setColor(Color.WHITE);
                    } else if(!StartDT.weapons[weaponIter * 3 + 2].isOwned()){
                        game.MFontS.setColor(Color.YELLOW);
                        game.MFontS.draw(
                                stage.getBatch(),
                                game.textSystem.get("level_required") + ": " + StartDT.weapons[weaponIter * 3 + 2].getPlayerXp(),
                                0.5f * (stage.getWidth() - game.MFontS.getWidth(game.textSystem.get("level_required") + ": " + StartDT.weapons[weaponIter * 3 + 2].getPlayerXp())),
                                0.35f * stage.getHeight() + 2.0f * game.MFontS.getHeight("A")
                        );
                        game.MFontS.setColor(Color.WHITE);
                    }
                    String text;
                    text = game.textSystem.get("n_stick_" + (((weaponIter * 3) / 3) + 1));
                    game.MFontS.draw(
                            stage.getBatch(),
                            text,
                            0.5f * (stage.getWidth() - game.MFontS.getWidth(text)),
                            0.475f * stage.getHeight()
                    );
                }
            }
        } else if(weaponShow[3]){

        }
    }
    private void obfuscate(float delta){
        if(obf.isActive() && !(nextToPrison)){
            obf.deactivate(0.5f, delta);
        } else if(nextToPrison){
            if(obf.isActive()){
                game.setScreen(new Prison(game));
                dispose();
            } else {
                obf.activate(0.5f, delta);
            }
        }
    }
    private void guiInit(){
        moneyBag = game.atlas.createSprite("money_bag");
        moneyBag.setBounds(
                0.025f * stage.getWidth(),
                stage.getHeight() - 0.125f * stage.getWidth(),
                0.1f * stage.getWidth(),
                0.1f * stage.getWidth()
        );
        weaponGui = new DTGui(
                game, stage,
                0.05f * stage.getWidth(),
                0.1f * stage.getHeight(),
                0.9f * stage.getWidth(),
                0.8f * stage.getHeight(),
                new String[] {},
                1,
                game.MFontS
        );
    }
    private void weaponInit(){
        checked = new Subject(
                game.atlas.createSprite("checked"),
                0f,
                0f,
                0.22f * stage.getWidth(),
                0.44f * stage.getWidth()
        );
        checked.setVisible(false);
        spears = new Subject[StartDT.weapons.length / 3];
        for(int i = 0; i < spears.length; ++i){
            spears[i] = new Subject(
                    game.atlas.createSprite("spear", i + 1),
                    0.11f * stage.getWidth() + (i % 3) * 0.28f * stage.getWidth(),
                    0.495f * stage.getHeight(),
                    0.22f * stage.getWidth(),
                    0.44f * stage.getWidth()
            );
            spears[i].addListener(new ClickListener(){
               @Override
                public void clicked(InputEvent inputEvent, float x, float y){
                   checked.setVisible(true);
                   checked.setPosition(inputEvent.getTarget().getX(), inputEvent.getTarget().getY());
                   select.get().setVisible(false);
                   buy.get().setVisible(false);
                   if(inputEvent.getTarget().getX() + 10 >= 0.67f * stage.getWidth()){
                       if(StartDT.weapons[(weaponIter + 2) * 3].isOwned()){
                           select.get().setVisible(true);
                           if(StartDT.weapons[(weaponIter + 2) * 3].isSelected()){
                               select.get().setText(game.textSystem.get("checked"));
                               select.get().setChecked(true);
                           } else {
                               select.get().setText(game.textSystem.get("select"));
                               select.get().setChecked(false);
                           }
                       } else {
                           buy.get().setVisible(true);
                       }
                   } else if(inputEvent.getTarget().getX() + 10 >= 0.39f * stage.getWidth()){
                       if(StartDT.weapons[(weaponIter + 1) * 3].isOwned()){
                           select.get().setVisible(true);
                           if(StartDT.weapons[(weaponIter + 1) * 3].isSelected()){
                               select.get().setText(game.textSystem.get("checked"));
                               select.get().setChecked(true);
                           } else {
                               select.get().setText(game.textSystem.get("select"));
                               select.get().setChecked(false);
                           }
                       } else {
                           buy.get().setVisible(true);
                       }
                   } else {
                       if(StartDT.weapons[weaponIter * 3].isOwned()){
                           select.get().setVisible(true);
                           if(StartDT.weapons[weaponIter * 3].isSelected()){
                               select.get().setText(game.textSystem.get("checked"));
                               select.get().setChecked(true);
                           } else {
                               select.get().setText(game.textSystem.get("select"));
                               select.get().setChecked(false);
                           }
                       } else {
                           buy.get().setVisible(true);
                       }
                   }
               }
            });
            spears[i].setVisible(false);
        }
        bows = new Subject[StartDT.weapons.length / 3];
        for(int i = 0; i < bows.length; ++i){
            bows[i] = new Subject(
                    game.atlas.createSprite("bow", i + 1),
                    0.11f * stage.getWidth() + (i % 3) * 0.28f * stage.getWidth(),
                    0.495f * stage.getHeight(),
                    0.22f * stage.getWidth(),
                    0.44f * stage.getWidth()
            );
            bows[i].addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent inputEvent, float x, float y){
                    checked.setVisible(true);
                    checked.setPosition(inputEvent.getTarget().getX(), inputEvent.getTarget().getY());
                    select.get().setVisible(false);
                    buy.get().setVisible(false);
                    if(inputEvent.getTarget().getX() + 10 >= 0.67f * stage.getWidth()){
                        if(StartDT.weapons[(weaponIter + 2) * 3 + 1].isOwned()){
                            select.get().setVisible(true);
                            if(StartDT.weapons[(weaponIter + 2) * 3 + 1].isSelected()){
                                select.get().setText(game.textSystem.get("checked"));
                                select.get().setChecked(true);
                            } else {
                                select.get().setText(game.textSystem.get("select"));
                                select.get().setChecked(false);
                            }
                        } else {
                            buy.get().setVisible(true);
                        }
                    } else if(inputEvent.getTarget().getX() + 10 >= 0.39f * stage.getWidth()){
                        if(StartDT.weapons[(weaponIter + 1) * 3 + 1].isOwned()){
                            select.get().setVisible(true);
                            if(StartDT.weapons[(weaponIter + 1) * 3 + 1].isSelected()){
                                select.get().setText(game.textSystem.get("checked"));
                                select.get().setChecked(true);
                            } else {
                                select.get().setText(game.textSystem.get("select"));
                                select.get().setChecked(false);
                            }
                        } else {
                            buy.get().setVisible(true);
                        }
                    } else {
                        if(StartDT.weapons[weaponIter * 3 + 1].isOwned()){
                            select.get().setVisible(true);
                            if(StartDT.weapons[weaponIter * 3 + 1].isSelected()){
                                select.get().setText(game.textSystem.get("checked"));
                                select.get().setChecked(true);
                            } else {
                                select.get().setText(game.textSystem.get("select"));
                                select.get().setChecked(false);
                            }
                        } else {
                            buy.get().setVisible(true);
                        }
                    }
                }
            });
            bows[i].setVisible(false);
        }
        sticks = new Subject[StartDT.weapons.length / 3];
        for(int i = 0; i < sticks.length; ++i){
            sticks[i] = new Subject(
                    game.atlas.createSprite("stick", i + 1),
                    0.11f * stage.getWidth() + (i % 3) * 0.28f * stage.getWidth(),
                    0.495f * stage.getHeight(),
                    0.22f * stage.getWidth(),
                    0.44f * stage.getWidth()
            );
            sticks[i].addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent inputEvent, float x, float y){
                    checked.setVisible(true);
                    checked.setPosition(inputEvent.getTarget().getX(), inputEvent.getTarget().getY());
                    select.get().setVisible(false);
                    buy.get().setVisible(false);
                    if(inputEvent.getTarget().getX() + 10 >= 0.67f * stage.getWidth()){
                        if(StartDT.weapons[(weaponIter + 2) * 3 + 2].isOwned()){
                            select.get().setVisible(true);
                            if(StartDT.weapons[(weaponIter + 2) * 3 + 2].isSelected()){
                                select.get().setText(game.textSystem.get("checked"));
                                select.get().setChecked(true);
                            } else {
                                select.get().setText(game.textSystem.get("select"));
                                select.get().setChecked(false);
                            }
                        } else {
                            buy.get().setVisible(true);
                        }
                    } else if(inputEvent.getTarget().getX() + 10 >= 0.39f * stage.getWidth()){
                        if(StartDT.weapons[(weaponIter + 1) * 3 + 2].isOwned()){
                            select.get().setVisible(true);
                            if(StartDT.weapons[(weaponIter + 1) * 3 + 2].isSelected()){
                                select.get().setText(game.textSystem.get("checked"));
                                select.get().setChecked(true);
                            } else {
                                select.get().setText(game.textSystem.get("select"));
                                select.get().setChecked(false);
                            }
                        } else {
                            buy.get().setVisible(true);
                        }
                    } else {
                        if(StartDT.weapons[weaponIter * 3 + 2].isOwned()){
                            select.get().setVisible(true);
                            if(StartDT.weapons[weaponIter * 3 + 2].isSelected()){
                                select.get().setText(game.textSystem.get("checked"));
                                select.get().setChecked(true);
                            } else {
                                select.get().setText(game.textSystem.get("select"));
                                select.get().setChecked(false);
                            }
                        } else {
                            buy.get().setVisible(true);
                        }
                    }
                }
            });
            sticks[i].setVisible(false);
        }
    }
    private void guiRender(){
        if(weaponShow[0]){
            if(weaponGui.isCloseClicked()) {
                if (!posters[0].hasActions())
                    posters[0].addAction(Actions.moveTo(posters[0].getX() + posters[0].getWidth(), posters[0].getY(), 0.75f));
                weaponShow[0] = false;
                weaponIter = 0;
                for(Actor act : spears){ act.setVisible(false); }
                next.get().setVisible(false);
                prev.get().setVisible(false);
                checked.setVisible(false);
                buy.get().setVisible(false);
                select.get().setVisible(false);
                toPrison.get().setVisible(true);
            }
        }
        if(weaponShow[0] && !posters[0].hasActions()) {
            next.get().setVisible(true);
            prev.get().setVisible(true);
            weaponGui.setDisplayed(true);
            toPrison.get().setVisible(false);
            for(Actor act : spears){ act.setVisible(false); }
            for(int i = weaponIter; i < weaponIter + 3; ++i){
                spears[i].setX(0.11f * stage.getWidth() + (i - weaponIter) * 0.28f * stage.getWidth());
                spears[i].setVisible(true);
            }
            game.MFontS.draw(
                    stage.getBatch(),
                    game.textSystem.get("spears"),
                    0.5f * (stage.getWidth() - game.MFontS.getWidth(game.textSystem.get("spears"))),
                    0.85f * stage.getHeight()
            );
        }

        if(weaponShow[1]){
            if(weaponGui.isCloseClicked()) {
                if (!posters[1].hasActions())
                    posters[1].addAction(Actions.moveTo(posters[1].getX() + posters[1].getWidth(), posters[1].getY(), 0.75f));
                weaponShow[1] = false;
                weaponIter = 0;
                for(Actor act : bows){ act.setVisible(false); }
                next.get().setVisible(false);
                prev.get().setVisible(false);
                checked.setVisible(false);
                buy.get().setVisible(false);
                select.get().setVisible(false);
                toPrison.get().setVisible(true);
            }
        }
        if(weaponShow[1] && !posters[1].hasActions()) {
            next.get().setVisible(true);
            prev.get().setVisible(true);
            weaponGui.setDisplayed(true);
            toPrison.get().setVisible(false);
            for(Actor act : bows){ act.setVisible(false); }
            for(int i = weaponIter; i < weaponIter + 3; ++i){
                bows[i].setX(0.11f * stage.getWidth() + (i - weaponIter) * 0.28f * stage.getWidth());
                bows[i].setVisible(true);
            }
            game.MFontS.draw(
                    stage.getBatch(),
                    game.textSystem.get("cowbows"),
                    0.5f * (stage.getWidth() - game.MFontS.getWidth(game.textSystem.get("cowbows"))),
                    0.85f * stage.getHeight()
            );
        }

        if(weaponShow[2]){
            if(weaponGui.isCloseClicked()) {
                if (!posters[2].hasActions())
                    posters[2].addAction(Actions.moveTo(posters[2].getX() - posters[2].getWidth(), posters[2].getY(), 0.75f));
                weaponShow[2] = false;
                weaponIter = 0;
                for(Actor act : sticks){ act.setVisible(false); }
                next.get().setVisible(false);
                prev.get().setVisible(false);
                checked.setVisible(false);
                buy.get().setVisible(false);
                select.get().setVisible(false);
                toPrison.get().setVisible(true);
            }
        }
        if(weaponShow[2] && !posters[2].hasActions()) {
            next.get().setVisible(true);
            prev.get().setVisible(true);
            weaponGui.setDisplayed(true);
            toPrison.get().setVisible(false);
            for(Actor act : sticks){ act.setVisible(false); }
            for(int i = weaponIter; i < weaponIter + 3; ++i){
                sticks[i].setX(0.11f * stage.getWidth() + (i - weaponIter) * 0.28f * stage.getWidth());
                sticks[i].setVisible(true);
            }
            game.MFontS.draw(
                    stage.getBatch(),
                    game.textSystem.get("sticks"),
                    0.5f * (stage.getWidth() - game.MFontS.getWidth(game.textSystem.get("sticks"))),
                    0.85f * stage.getHeight()
            );
        }

        if(weaponShow[3]){
            if(weaponGui.isCloseClicked()) {
                if (!posters[3].hasActions())
                    posters[3].addAction(Actions.moveTo(posters[3].getX() - posters[3].getWidth(), posters[3].getY(), 0.75f));
                weaponShow[3] = false;
                weaponIter = 0;
            }
        }
        if(weaponShow[3] && !posters[3].hasActions()) {
            weaponGui.setDisplayed(true);
        }
    }

    @Override
    public void show() {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        weaponShow = new boolean[] {false, false, false, false};

        background = game.atlas.createSprite("shop");
        background.setBounds(0f, 0f, stage.getWidth(), stage.getHeight());
        hero = new Sprite[4];
        for(int i = 0; i < 4; ++i) {
            hero[i] = game.atlas.createSprite("hero_back", i + 1);
            hero[i].setBounds(
                    0.3f * stage.getWidth(),
                    -0.4f * stage.getWidth(),
                    0.4f * stage.getWidth(),
                    0.8f * stage.getWidth()
            );
        }

        weaponInit();
        buttonInit();
        guiInit();

        stage.addActor(toPrison.get());
        stage.addActor(weaponGui);
        stage.addActor(checked);
        for(Actor act : spears){ stage.addActor(act); }
        for(Actor act : bows){ stage.addActor(act); }
        for(Actor act : sticks){ stage.addActor(act); }
        stage.addActor(next.get());
        stage.addActor(prev.get());
        stage.addActor(buy.get());
        stage.addActor(select.get());

        obf = new Obfuscation(game.atlas.createSprite("obfuscation"), true);
        stage.addActor(obf);

        StartDT.mainMusic.play();

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        time += delta;
        if(time > 0.5f){
            time = 0f;
            if(iter == 3){
                iter = 0;
            } else {
                iter++;
            }
        }

        stage.getBatch().begin();
        background.draw(stage.getBatch());
        hero[iter].draw(stage.getBatch());
        moneyBag.draw(stage.getBatch());
        game.MFontU.draw(
                stage.getBatch(),
                ": " + StartDT.player.getMoney(),
                moneyBag.getX() + 1.05f * moneyBag.getWidth(),
                moneyBag.getY() + 0.5f * (moneyBag.getHeight() + game.MFontU.getHeight("A"))
        );
        stage.getBatch().end();

        stage.act(delta);
        stage.draw();

        stage.getBatch().begin();
        textDraw();
        guiRender();
        stage.getBatch().end();

        obfuscate(delta);

        if(Gdx.input.isKeyJustPressed(Input.Keys.BACK)){
            StartDT.mainMusic.stop();
            game.setScreen(new Game(game, false));
            dispose();
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.HOME)){
            StartDT.mainMusic.stop();
            dispose();
            Gdx.app.exit();
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {
        StartDT.mainMusic.stop();
        StartDT.player.saveStats();
    }

    @Override
    public void resume() {
        game.setScreen(new Splash(game));
        dispose();
    }

    @Override
    public void hide() {
        StartDT.player.saveStats();
    }

    @Override
    public void dispose() {
        StartDT.player.saveStats();
    }
}
