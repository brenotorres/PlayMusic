package GUI;


import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;
import java.util.Vector;

import javax.sound.sampled.UnsupportedAudioFileException;

import javafx.scene.control.Button;

import CORE.Cliente;
import CORE.Mp3;
import CORE.PlayerMusic;
import CORE.RepositorioMusica;
import CORE.interfacePlayer;
import CORE.RepositorioMusica;

import com.sun.corba.se.spi.orbutil.fsm.Action;
import com.sun.glass.events.KeyEvent;
import com.sun.javafx.geom.Rectangle;
import com.sun.webpane.platform.ContextMenu;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Point3D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Separator;
import javafx.scene.control.Skin;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.paint.Color;

public class Gui extends Application {

	private interfacePlayer i = new PlayerMusic();
	private Scene cena ;
	private int state = UNKNOWN;	
	private static final int UNKNOWN = -1;
	private static final int PLAYING = 0;
	private static final int PAUSED = 1;
	private static final int STOPPED = 2;
	private static final int SEEKING = 3;
	private boolean mute = false;
	private float retornoMute;
	private String ipserver = "null";
	private String diretorio = "C:/Caja/";
	private Vector<Mp3> vectorMp3 = new Vector<Mp3>();
	private Cliente cl;
	private TableView table = new TableView();
	private Slider volume = new Slider();
	final ObservableList<Mp3> data = FXCollections.observableArrayList();


	public static void main(String[] args) {
		launch(); 
	}

	@Override
	public void start(final Stage palco) throws Exception { 
		final VBox raiz = new VBox(10); 
		final VBox configini = new VBox(10);
		cena = new Scene(configini, 300, 100); 

		Label ip = new Label("IP do servidor");
		final TextField txtIp = new TextField("");
		txtIp.setTooltip(new Tooltip("Digite o IP do servidor para poder começar a usar o Cajá corretamente"));
		Button config = new Button("Configurar");

		configini.getChildren().addAll(ip, txtIp, config);

		config.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent evento){
				//try {
				cena = new Scene(raiz, 600, 400);
				setIP(txtIp.getText());
				palco.setScene(cena);
				palco.show();
				//					InetAddress ip;
				//					ip = InetAddress.getByName(txtIp.getText());
				//					cl = new Cliente(diretorio, ip) ;
				//					vectorMp3 = cl.solicitarlista();

				RepositorioMusica a = new RepositorioMusica(diretorio);
				try {
					vectorMp3 = a.gerarLista();
				} catch (UnsupportedAudioFileException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if(!vectorMp3.isEmpty()){
					System.out.println("HEREERE");
					int i = 0;
					while(i<vectorMp3.size()){
						data.add(vectorMp3.get(i));
						i++;
					}
				}
				//				} catch (UnknownHostException e) {
				//					e.printStackTrace();
				//				}
			}
		});



		Label lblMensagem = new Label(); 
		lblMensagem.setText("Masoque");
		Font font = new Font(100);
		lblMensagem.setFont(font);
		lblMensagem.setLayoutY(500);

		//Separador para ver se deixa organizado
		Separator separadorHorizontal = new Separator(); // 7
		Separator separadorVertical = new Separator(Orientation.VERTICAL); // 8

		TabPane tabPane = new TabPane();

		Tab tab = new Tab();
		tab.setText("Player");
		tab.setContent(Player1());
		tab.setClosable(false);

		Tab tab2 = new Tab();
		tab2.setText("Download");
		tab2.setContent(lblMensagem);
		tab2.setClosable(false);

		Tab tab3 = new Tab();
		tab3.setText("Configurações");
		tab3.setContent(Config());
		tab3.setClosable(false);

		tabPane.getTabs().addAll(tab, tab2, tab3);
		raiz.getChildren().add(tabPane);

		palco.setTitle("Cajá player"); // 9
		palco.setScene(cena); // 10
		palco.show(); // 1

	}


	private Pane Player1() {

		VBox vbox = new VBox();
		table.setEditable(true);

		TableColumn musicCol = new TableColumn("Música");
		musicCol.setPrefWidth(150);
		musicCol.getWidth();
		musicCol.setCellValueFactory(new PropertyValueFactory<Mp3, String>("nome"));

		TableColumn albumCol = new TableColumn("Álbum");
		albumCol.setPrefWidth(150);
		albumCol.setCellValueFactory(new PropertyValueFactory<Mp3, String>("album"));

		TableColumn artistaCol = new TableColumn("Artista");
		artistaCol.setPrefWidth(150);
		artistaCol.setCellValueFactory(new PropertyValueFactory<Mp3, String>("autor"));

		TableColumn generoCol = new TableColumn("Gênero");
		generoCol.setPrefWidth(150);
		generoCol.setCellValueFactory(new PropertyValueFactory<Mp3, String>("genero"));

		table.getColumns().addAll(musicCol, albumCol, artistaCol, generoCol);

		table.setItems(data);


		Separator separadorHorizontal = new Separator();

		Slider deslizante = new Slider(); // 9
		deslizante.setMaxWidth(590);
		deslizante.setMin(0); 
		deslizante.setShowTickLabels(false); // 10
		deslizante.setShowTickMarks(false); // 11
		//deslizante.setTooltip(new Tooltip("O controle deslizante tem um valor numérico de acordo com sua posição"));

		vbox.getChildren().addAll(table, separadorHorizontal, deslizante, Menuplayer());

		//border.setBottom(butao);
		//butao.setOnAction();
		//border.setBottom.()
		//border.setRight(createButtonColumn());  
		//border.setBottom(createButtonRow());  // Uses a tile pane for sizing
		//border.setBottom(createButtonBox());  // Uses an HBox, no sizing 

		return vbox;
	}

	Node Menuplayer(){
		HBox hbox = new HBox();
		hbox.setSpacing(5);
		
		volume.setMaxWidth(100); 
		volume.setShowTickLabels(false); // 10
		volume.setShowTickMarks(false); // 11
		volume.setTranslateY(volume.getTranslateY()+17);
		volume.setMin(-80.0);
		volume.setMax(6.0206);
		volume.setValue(-10);
		//deslizante.setTooltip(new Tooltip("O controle deslizante tem um valor numérico de acordo com sua posição"));

		final Image iplay = new Image(getClass().getResourceAsStream("play.png"));
		final Image ipause = new Image(getClass().getResourceAsStream("pause.png"));
		final Image ifone = new Image(getClass().getResourceAsStream("fone.png"));
		final Image istop = new Image(getClass().getResourceAsStream("stop.png"));
		final Image imute = new Image(getClass().getResourceAsStream("mute.png"));

		final Button botPlay = new Button(); 	
		final Button botPause = new Button();
		final Button botStop = new Button();
		final Button botMute = new Button();

		botPlay.setGraphic(new ImageView(iplay));
		botPlay.setContentDisplay(ContentDisplay.CENTER);
		botPause.setGraphic(new ImageView(ipause));
		botStop.setGraphic(new ImageView(istop));
		botMute.setGraphic(new ImageView(ifone));

		botPlay.setStyle("-fx-base: transparent; ");//deixar transparente
		botPlay.setFocusTraversable(false);//tirar borda
		botStop.setStyle("-fx-base: transparent;");
		botStop.setFocusTraversable(false);
		botMute.setStyle("-fx-base: transparent;");
		botMute.setFocusTraversable(false);

		volume.valueProperty().addListener(new ChangeListener<Number>() {
			public void changed(ObservableValue<? extends Number> ov, Number old_v, Number new_v){
				i.set_volume(new_v.longValue());
			}
		});

		botPlay.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent evento) {
				if(state != PLAYING){
					System.out.println(((Mp3)table.getSelectionModel().getSelectedItem()).getNome());
					File f = new File(diretorio+((Mp3)table.getSelectionModel().getSelectedItem()).getNome());
					i.play(f);
					i.set_volume((float)volume.getValue());
					//System.out.println(i.get_minimo());
					//while(i.get_maximo()==i.get_minimo()){}
					botPlay.setGraphic(new ImageView(ipause));					
					state = PLAYING;					
				}else{
					i.pause();
					state = PAUSED;
					botPlay.setGraphic(new ImageView(iplay));
				}
			}
		});

		botStop.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent evento){
				if(state==PLAYING||state==PAUSED){
					i.stop();
				}
				if(state==PLAYING){
					botPlay.setGraphic(new ImageView(iplay));
				}
				state=STOPPED;
			}
		});

		botMute.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle (ActionEvent evento){
				if(state == PLAYING||state == PAUSED){	
					if(mute){
						mute = false;
						i.set_volume(retornoMute);
						botMute.setGraphic( new ImageView(ifone) );
						volume.setValue(retornoMute);
					}else{
						mute = true;
						retornoMute = i.get_volumeAtual();
						i.set_volume(i.get_minimo());
						volume.setValue(i.get_minimo());
						botMute.setGraphic( new ImageView(imute) );
					}
				}	
			}
		});
		

		hbox.setSpacing(10);
		hbox.getChildren().addAll(botPlay, botStop, botMute, volume);
		return hbox;
	}

	private Node Config(){
		VBox vbox = new VBox();
		//vbox.setSpacing(10);

		Label ip = new Label("IP do servidor");
		Label barran = new Label("\n\n");
		Label Diretorio = new Label("Diretorio de download");
		final TextField txtIp = new TextField(getIP());
		txtIp.setTooltip(new Tooltip("Digite o IP do servidor para poder começar a usar o Cajá corretamente"));

		txtIp.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent evento){
				txtIp.clear();
			}
		});

		final TextField txtDiretorio = new TextField(diretorio);
		txtDiretorio.setTooltip(new Tooltip("Diretorio de onde as músicas serão salvas "));
		final Label configurado = new Label("\nConfigurado com sucesso!");
		configurado.setTextFill(Color.web("#0076a3"));
		configurado.setVisible(false);


		Button config = new Button("Configurar");
		config.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent evento){
				configurado.setVisible(true);

				setIP(txtIp.getText());
				System.out.println(txtIp.getText());

				//adicionar mais coisas
			}
		});

		vbox.getChildren().addAll(ip, txtIp, Diretorio, txtDiretorio, configurado, config);

		return vbox;
	}

	void setIP(String k){
		this.ipserver = k;
	}

	String getIP(){
		System.out.println(this.ipserver);
		return this.ipserver;
	}

}
