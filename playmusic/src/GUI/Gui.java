package GUI;

import java.io.File;

import javafx.scene.control.Button;


import CORE.PlayerMusic;
import CORE.interfacePlayer;

import com.sun.javafx.geom.Rectangle;
import com.sun.webpane.platform.ContextMenu;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Separator;
import javafx.scene.control.Skin;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.paint.Color;

public class Gui extends Application { // 1

	private interfacePlayer i = new PlayerMusic();
	private Scene cena ;

	private int state = UNKNOWN;	
	private static final int UNKNOWN = -1;
	private static final int PLAYING = 0;
	private static final int PAUSED = 1;
	private static final int STOPPED = 2;
	private static final int SEEKING = 3;


	public static void main(String[] args) {
		launch(); // chama a aplicação
	}

	@Override
	public void start(Stage palco) throws Exception { 
		VBox raiz = new VBox(10); 
		cena = new Scene(raiz, 600, 600); 

		Label lblMensagem = new Label(); 
		lblMensagem.setText("Masoque");

		Font font = new Font(100);
		lblMensagem.setFont(font);


		lblMensagem.setLayoutY(500);

		//Separador para ver se deixa organizado
		Separator separadorHorizontal = new Separator(); // 7
		Separator separadorVertical = new Separator(Orientation.VERTICAL); // 8

		TabPane tabPane = new TabPane();

		//ContextMenu contexMenu = new 

		Tab tab = new Tab();
		tab.setText("Player");
		tab.setContent(Player1());
		tab.setClosable(false);

		Tab tab2 = new Tab();
		tab2.setText("Download");
		tab2.setContent(lblMensagem);
		tab2.setClosable(false);

		tabPane.getTabs().addAll(tab, tab2);

		//raiz.getChildren().addAll(tabPane, lblMensagem, circulo, separadorHorizontal, deslizante, separadorVertical); // 7
		raiz.getChildren().add(tabPane);

		palco.setTitle("Cajá player"); // 9
		palco.setScene(cena); // 10
		palco.show(); // 1

	}


	private Pane Player1() {
		BorderPane border = new BorderPane();
		border.setPadding(new Insets(20, 0, 20, 20));

		ListView<String> lvList = new ListView<String>(); 
		ObservableList<String> items = FXCollections.observableArrayList (
				"Musica1", "Musica2 - artista", "Musica3 - artista", 
				"opa", "lelele", "coisa","Musica1", "Musica2 - artista", "Musica3 - artista", 
				"opa", "lelele", "coisa","Musica1", "Musica2 - artista", "Musica3 - artista", 
				"opa", "lelele", "coisa","Musica1", "Musica2 - artista", "Musica3 - artista", 
				"opa", "lelele", "coisa","Musica1", "Musica2 - artista", "Musica3 - artista", 
				"opa", "lelele", "coisa","Musica1", "Musica2 - artista", "Musica3 - artista", 
				"opa", "lelele", "coisa","Musica1", "Musica2 - artista", "Musica3 - artista", 
				"opa", "lelele", "coisa","Musica1", "Musica2 - artista", "Musica3 - artista", 
				"opa", "lelele", "coisa");
		lvList.setItems(items);	

		//lvList.setMaxHeight(Control.USE_PREF_SIZE);
		lvList.setPrefWidth(cena.getWidth());

		border.setLeft(lvList);
		Separator separadorHorizontal = new Separator();
		border.setCenter(separadorHorizontal);

		border.setBottom(Menuplayer());
		//border.setBottom(butao);
		//butao.setOnAction();
		//border.setBottom.()
		//border.setRight(createButtonColumn());  
		//border.setBottom(createButtonRow());  // Uses a tile pane for sizing
		//border.setBottom(createButtonBox());  // Uses an HBox, no sizing 


		return border;
	}

	Node Menuplayer(){
		HBox hbox = new HBox();
		hbox.setSpacing(5);

		Slider deslizante = new Slider(); // 9
		deslizante.setMaxWidth(150);
		deslizante.setMin(4); 
		deslizante.setShowTickLabels(false); // 10
		deslizante.setShowTickMarks(false); // 11
		//deslizante.setTooltip(new Tooltip("O controle deslizante tem um valor numérico de acordo com sua posição"));

		Slider volume = new Slider(); // 9
		volume.setMaxWidth(50);
		volume.setMin(4); 
		volume.setShowTickLabels(false); // 10
		volume.setShowTickMarks(false); // 11
		volume.setRotate(337);
		//deslizante.setTooltip(new Tooltip("O controle deslizante tem um valor numérico de acordo com sua posição"));


		final Image iplay = new Image(getClass().getResourceAsStream("play.png"));
		final Image ipause = new Image(getClass().getResourceAsStream("pause.png"));
		final Image ifone = new Image(getClass().getResourceAsStream("fone.png"));
		final Image istop = new Image(getClass().getResourceAsStream("stop.png"));

		final Button botPlay = new Button(); 	
		Button botPause = new Button();
		Button botStop = new Button();
		Button botMute = new Button();

		botPlay.setGraphic(new ImageView(iplay));
		botPause.setGraphic(new ImageView(ipause));
		botStop.setGraphic(new ImageView(istop));
		botMute.setGraphic(new ImageView(ifone));

		botPlay.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent evento) {
				if(state != PLAYING){
					File f = new File("Manguetown.mp3");
					i.play(f);
					state = PLAYING;
					botPlay.setGraphic(new ImageView(ipause));
				}else{
					i.pause();
					state = PAUSED;
					botPlay.setGraphic(new ImageView(iplay));

				}
			}
		});

		botPause.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent evento) {
				File f = new File("Manguetown.mp3");
				i.pause();
				botPlay.setVisible(true);
			}
		});


		hbox.setSpacing(10);
		hbox.getChildren().addAll(botPlay, botPause, botStop, deslizante, botMute, volume);
		return hbox;
	}

}
