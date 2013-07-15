package fr.lirmm.smile.rollingcat.screen;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;
import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getStage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import fr.lirmm.smile.rollingcat.GameConstants;
import fr.lirmm.smile.rollingcat.RollingCat;

public class SplashScreen implements Screen{
	
	private RollingCat game;
	private Stage stage;
	private Texture splashTexture;
	
	public SplashScreen(RollingCat game){
		this.game = game;
	}
	
	@Override
	public void render(float delta) {
		stage.act();
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
        TextureRegion splashRegion = new TextureRegion(splashTexture, 0, 0, splashTexture.getWidth(), splashTexture.getHeight() );
        Image splashImage = new Image(splashRegion);
        splashImage.setWidth(width);
        splashImage.setHeight(height);
        
        splashImage.getColor().a = 0f;

        splashImage.addAction( sequence( fadeIn( 0.75f ), fadeOut( 0.75f ),
                new Action() {
                    @Override
                    public boolean act(float delta)
                    {	
                        game.setScreen(new LoginScreen(game));
                        return true;
                    }
                }));
        
        stage.addActor(splashImage);
	}

	@Override
	public void show() {
		splashTexture = new Texture(GameConstants.TEXTURE_SPLASHSCREEN);
		splashTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		stage = getStage();
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		Gdx.app.log(RollingCat.LOG, "disposing...");
		splashTexture.dispose();
	}

}
