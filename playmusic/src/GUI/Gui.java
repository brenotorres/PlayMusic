package GUI;

import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;
import java.util.Vector;

import javax.sound.sampled.UnsupportedAudioFileException;

import sun.awt.AWTAccessor.MenuAccessor;
import sun.awt.image.PixelConverter.Bgrx;
import sun.reflect.generics.tree.BottomSignature;

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
import com.sun.org.apache.bcel.internal.generic.GETSTATIC;
import com.sun.org.apache.regexp.internal.recompile;
import com.sun.webpane.platform.ContextMenu;
import com.sun.xml.internal.ws.api.pipe.NextAction;

import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongPropertyBase;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.paint.Color;


public class Gui extends Application {

	public double tempo;
	public  long seek=0;
	public static boolean tre = false;
	public static interfacePlayer i = new PlayerMusic();
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
	static TableView table = new TableView();
	private TableView playlist = new TableView();
	private TableView tabledown = new TableView();
	private Slider volume = new Slider();
	final Slider reproducao = new Slider(); // 9
	private File f;
	final ObservableList<Mp3> data = FXCollections.observableArrayList();
	public static LongPropertyBase a;
	public boolean thread = false;
	private Label bx;
	final ObservableList<Mp3> datagenero = FXCollections.observableArrayList();
	final ObservableList<Mp3> dataArtista = FXCollections.observableArrayList();
	private Menu menuGen = new Menu("Genêro");
	private Menu menuArtist = new Menu("Artista");
	private Menu menuCustons = new Menu("Customizadas");

	//static Thread repro = new Reproducao();

	public static void main(String[] args) {
		launch(); 
	}


	@Override
	public void start(final Stage palco) throws Exception { 
		palco.getIcons().add(new Image("caja.png"));
		final VBox raiz = new VBox(10); 
		final VBox configini = new VBox(10);
		cena = new Scene(configini, 300, 200); 
		//Color c = Color.web("0x01022FF",1.0);// blue as a hex web value, explicit alpha
		cena.setFill(Color.GRAY);//escolher a cor....

		Label ip = new Label("IP do servidor");
		final TextField txtIp = new TextField("");
		txtIp.setTooltip(new Tooltip("Digite o IP do servidor para poder começar a usar o Cajá corretamente"));
		Button config = new Button("Configurar");

		configini.getChildren().addAll(ip, txtIp, config);
		
		config.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent evento){
				//cena = new Scene(raiz, 800, 600);
				cena.setRoot(raiz);
				palco.setWidth(800);
				palco.setHeight(600);

				setIP(txtIp.getText());

				playlist.prefWidthProperty().bind(cena.widthProperty());
				playlist.prefHeightProperty().bind(cena.heightProperty());
				table.prefWidthProperty().bind(cena.widthProperty());
				table.prefHeightProperty().bind(cena.heightProperty());
				
				System.out.println(table.heightProperty());
				table.prefHeight(500);
				palco.setScene(cena);
				palco.show();
				RepositorioMusica a = new RepositorioMusica(diretorio);
				try {
					vectorMp3 = a.gerarLista();
				} catch (UnsupportedAudioFileException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				datagenero();
				dataArtista();

				if(!vectorMp3.isEmpty()){
					int i = 0;
					while(i<vectorMp3.size()){
						data.add(vectorMp3.get(i));
						i++;
					}
				}
			}
		});

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
		tab2.setContent(DownloadTab());
		tab2.setClosable(false);

		Tab tab3 = new Tab();
		tab3.setText("Configurações");
		tab3.setContent(Config());
		tab3.setClosable(false);

		Tab tab4 = new Tab();
		tab4.setText("Playlists");
		tab4.setContent(Playlists());
		tab4.setClosable(false);

		tabPane.getTabs().addAll(tab, tab2, tab3, tab4);
		raiz.getChildren().add(tabPane);

		palco.setTitle("Cajá player"); // 9
		palco.setScene(cena); // 10

		palco.show(); // 1

	}




	private Pane Player1() {

		//HBox linharepro = new HBox();
		VBox vbox = new VBox();
		table.setEditable(true);

		TableColumn musicCol = new TableColumn("Música");

		musicCol.prefWidthProperty().bind(table.widthProperty().divide(4));
		musicCol.setCellValueFactory(new PropertyValueFactory<Mp3, String>("nome"));

		TableColumn albumCol = new TableColumn("Álbum");
		albumCol.prefWidthProperty().bind(table.widthProperty().divide(4));
		albumCol.setCellValueFactory(new PropertyValueFactory<Mp3, String>("album"));

		TableColumn artistaCol = new TableColumn("Artista");
		artistaCol.prefWidthProperty().bind(table.widthProperty().divide(4));
		artistaCol.setCellValueFactory(new PropertyValueFactory<Mp3, String>("autor"));

		TableColumn generoCol = new TableColumn("Gênero");
		generoCol.prefWidthProperty().bind(table.widthProperty().divide(4));
		generoCol.setCellValueFactory(new PropertyValueFactory<Mp3, String>("genero"));

		table.getColumns().addAll(musicCol, albumCol, artistaCol, generoCol);
		table.setItems(data);

		Separator separadorHorizontal = new Separator();

		reproducao.setMin(0); 
		reproducao.setMaxWidth(700);
		reproducao.setMax(10);
		reproducao.setShowTickLabels(false); // 10
		reproducao.setShowTickMarks(false); // 11
		reproducao.valueProperty().addListener(new ChangeListener<Number>() {

			public void changed(ObservableValue<? extends Number> ov, Number old_v, Number new_v){
				if(state == PLAYING){
					if( (new_v.longValue() > old_v.longValue()+2) || (new_v.longValue() < old_v.longValue()-2)  ){
						state = SEEKING;
						System.out.println(new_v.longValue()+ "novo valor ");
						seek = new_v.longValue();
						System.out.println("LISTENER");
						i.seek(new_v.longValue());
						state = PLAYING;
					}

				}
			}
		});

		a = new LongPropertyBase() {

			@Override
			public String getName() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Object getBean() {
				// TODO Auto-generated method stub
				return null;
			}
		};

		a.addListener(new ChangeListener<Number>() {
			public void changed(ObservableValue<? extends Number> ov, Number old_v, Number new_v){
				reproducao.setValue( new_v.longValue() + seek);
			}
		});

		vbox.getChildren().addAll( table, separadorHorizontal, reproducao, Menuplayer());

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
					seek = 0;
					Mp3 o = ((Mp3)table.getSelectionModel().getSelectedItem());
					System.out.println(((Mp3)table.getSelectionModel().getSelectedItem()).getNome());
					f = new File(diretorio+((Mp3)table.getSelectionModel().getSelectedItem()).getNome());
					i.play(f);
					vectorMp3.get(vectorMp3.indexOf(o)).incremeta();
					i.set_volume((float)volume.getValue());
					String t = ((Mp3)table.getSelectionModel().getSelectedItem()).getTempo();
					int index = 0;
					for( ; t.charAt(index) != ':' ; index++ ){}
					tempo = Double.parseDouble(t.substring(0, index)) * 60 + Double.parseDouble(t.substring(index+1)) ;
					thread = true;
					reproducao.setMax(tempo);
					System.out.println("TEMPO GUI"+tempo);
					tre = true;
					RpprMain as = new RpprMain();			

					//as.run();
					//repro.run();
					//while(index < 10){
					//a.set(i.Microseconds());
					//a.set(i.Microseconds());
					//index++;
					//}
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
	public Node DownloadTab(){
		VBox vbox = new VBox(20);

		TableColumn musicCol = new TableColumn("Música");
		musicCol.setPrefWidth(150);
		musicCol.getWidth();
		musicCol.setCellValueFactory(new PropertyValueFactory<Mp3, String>("nome"));

		TableColumn artistaCol = new TableColumn("Artista");
		artistaCol.setPrefWidth(150);
		artistaCol.setCellValueFactory(new PropertyValueFactory<Mp3, String>("autor"));

		TableColumn TamanhoCol = new TableColumn("Tamanho (KBytes)");
		TamanhoCol.setPrefWidth(150);
		TamanhoCol.setCellValueFactory(new PropertyValueFactory<Mp3, String>("tamanho"));

		TableColumn VelocidadeCol = new TableColumn("Velocidade");
		VelocidadeCol.setPrefWidth(150);

		TableColumn EstadoCol = new TableColumn("Estado");
		EstadoCol.setPrefWidth(150);

		tabledown.getColumns().addAll(musicCol, artistaCol, TamanhoCol, VelocidadeCol, EstadoCol);

		HBox hbox = new HBox(10);

		Button BaixarPause = new Button("BaixarPause PROCURAR ICONE");
		Button Parar = new Button("Parar PROCURAR ICONE");
		Button Recomecar = new Button("Recomecar PROCURAR ICONE");

		hbox.getChildren().addAll(BaixarPause, Parar, Recomecar);
		//passar aqui a lista de arquivos vinda do servidor.
		//table.setItems(data);

		vbox.getChildren().addAll(tabledown, hbox);
		return vbox;
	}

	public void datagenero(){
		String[] g = new String[125];
		boolean achou = false, nullo = false;
		int index = 0;
		for (int i = 0; i < vectorMp3.size(); i++) {
			for (int j = 0; j < 125 && !achou && !nullo; j++ ) {
				if(g[j]!=null){
					if( g[j].equals(vectorMp3.get(i).getGenero())){
						achou = true;
					}
				}else{
					nullo = true;
				}
			}
			if( achou == false ){
				g[index] = vectorMp3.get(i).getGenero();
				index++;
				System.out.println(vectorMp3.get(i).getGenero());
				System.out.println("ADD");
				datagenero.add( vectorMp3.get(i) );
			}
			achou = false;
			nullo = false;
		}

		for(Mp3 m : datagenero){
			//			if (m.getGenero().equals("File")) {
			//				item.setOnAction(new EventHandler<ActionEvent>() {
			//					@Override
			//					public void handle(ActionEvent arg0) {
			//						EventHandler<? super MouseEvent> listener2 = getListener(
			//								null, "close");
			//						listener2.handle(null);
			//
			//					}
			//				});	
			final String gen = m.getGenero();
			final MenuItem ab = new MenuItem(gen);
			ab.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent evento){
					Gerarplaygenero(gen);
				}
			});
			menuGen.getItems().add(ab);
		}
	}



	public void dataArtista(){
		String[] g = new String[200];
		boolean achou = false, nullo = false;
		int index = 0;
		for (int i = 0; i < vectorMp3.size(); i++) {
			for (int j = 0; j < 125 && !achou && !nullo; j++ ) {
				if(g[j]!=null){
					if( g[j].equals(vectorMp3.get(i).getAutor())){
						achou = true;
					}
				}else{
					nullo = true;
				}
			}
			if( achou == false ){
				g[index] = vectorMp3.get(i).getAutor();
				index++;
				dataArtista.add( vectorMp3.get(i) );
			}
			achou = false;
			nullo = false;
		}

		for(Mp3 m : dataArtista){
			//			if (m.getGenero().equals("File")) {
			//				item.setOnAction(new EventHandler<ActionEvent>() {
			//					@Override
			//					public void handle(ActionEvent arg0) {
			//						EventHandler<? super MouseEvent> listener2 = getListener(
			//								null, "close");
			//						listener2.handle(null);
			//
			//					}
			//				});	
			final String artista = m.getAutor();
			final MenuItem ab = new MenuItem(artista);
			ab.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent evento){
					Gerarplayartista(artista);
				}
			});
			menuArtist.getItems().add(ab);
		}

	}

	public void Gerarplaygenero(String genero){
		ObservableList<Mp3> dataplay = FXCollections.observableArrayList();
		for (int i = 0; i < vectorMp3.size(); i++) {
			if( genero.equals(vectorMp3.get(i).getGenero())){
				dataplay.add(vectorMp3.get(i));
			}								
		}

		playlist.setItems(dataplay);
	}

	public void Gerarplayartista(String artista){
		ObservableList<Mp3> dataplay = FXCollections.observableArrayList();

		for (int i = 0; i < vectorMp3.size(); i++) {
			if( artista.equals(vectorMp3.get(i).getAutor())){
				dataplay.add(vectorMp3.get(i));
			}								
		}

		playlist.setItems(dataplay);
	}

	public void Gerarplaytop10(){
		ObservableList<Mp3> datatop = FXCollections.observableArrayList();
		Mp3 tabcont[] = new Mp3[10];
		int menor = 0, indice = 0, edez=0; 

		if(vectorMp3.size()>9){
			for (int i = 0; i < vectorMp3.size(); i++) {				
				if(i>9){
					if( vectorMp3.get(i).getContador() > menor){
						tabcont[indice] = vectorMp3.get(i);
						menor = Integer.MAX_VALUE;
						for (int j = 0; j < tabcont.length; j++) {
							if(tabcont[j].getContador() < menor){
								menor = tabcont[j].getContador();
								indice = j;
							}
						}
					}
				}else{
					if(edez == 0){
						menor=vectorMp3.get(0).getContador();
						indice = 0;
					}else if(vectorMp3.get(i).getContador() < menor){
						menor = vectorMp3.get(i).getContador();
						indice = i;
					}
					tabcont[edez] = vectorMp3.get(i);				
					edez++;
				}
			}
		}


		for (int i = 0; i < tabcont.length; i++) {
			if(tabcont[i]!= null){
				System.out.println("add" + tabcont[i].getContador());
				datatop.add(tabcont[i]);
			}
		}

		playlist.setItems(datatop);
	}

	public void Gerarplaymenosouvidas(){
		ObservableList<Mp3> datatop = FXCollections.observableArrayList();
		Mp3 tabcont[] = new Mp3[10];
		int maior = 0, indice = 0, edez=0; 

		if(vectorMp3.size()>9){
			for (int i = 0; i < vectorMp3.size(); i++) {				
				if(i>9){
					if( vectorMp3.get(i).getContador() < maior){
						tabcont[indice] = vectorMp3.get(i);
						maior = 0;
						for (int j = 0; j < tabcont.length; j++) {
							if(tabcont[j].getContador() > maior){
								maior = tabcont[j].getContador();
								indice = j;
							}
						}
					}
				}else{
					if(edez == 0){
						maior = vectorMp3.get(0).getContador();
						indice = 0;
					}else if(vectorMp3.get(i).getContador() > maior){
						maior = vectorMp3.get(i).getContador();
						indice = i;
					}
					tabcont[edez] = vectorMp3.get(i);				
					edez++;
				}
			}
		}


		for (int i = 0; i < tabcont.length; i++) {
			if(tabcont[i]!= null){
				System.out.println("add" + tabcont[i].getContador());
				datatop.add(tabcont[i]);
			}
		}

		playlist.setItems(datatop);
	}

	
	private Node Playlists() {

		VBox vBox = new VBox(5);
		TableColumn playl = new TableColumn("Playlist");
		playl.setPrefWidth(300);
		playl.setCellValueFactory(new PropertyValueFactory<Mp3, String>("nome"));
		playlist.getColumns().add(playl);
		
		playlist.prefHeightProperty().bind(cena.heightProperty());
		playlist.prefWidthProperty().bind(cena.widthProperty());
		
		final Image iplay = new Image(getClass().getResourceAsStream("play.png"));
		final Image ipause = new Image(getClass().getResourceAsStream("pause.png"));
		final Image ifone = new Image(getClass().getResourceAsStream("fone.png"));
		final Image istop = new Image(getClass().getResourceAsStream("stop.png"));
		final Image imute = new Image(getClass().getResourceAsStream("mute.png"));
		final Image iantes = new Image(getClass().getResourceAsStream("anterior.png"));
		final Image iproximo = new Image(getClass().getResourceAsStream("proximo.png"));


		final Button botPlay = new Button(); 	
		final Button botPause = new Button();
		final Button botStop = new Button();
		final Button botMute = new Button();
		final Button botAnt = new Button();
		final Button botPro = new Button();

		botPlay.setGraphic(new ImageView(iplay));
		botPause.setGraphic(new ImageView(ipause));
		botStop.setGraphic(new ImageView(istop));
		botMute.setGraphic(new ImageView(ifone));
		botAnt.setGraphic(new ImageView(iantes));
		botPro.setGraphic(new ImageView(iproximo));


		botPlay.setStyle("-fx-base: transparent; ");//deixar transparente
		botPlay.setFocusTraversable(false);//tirar borda
		botStop.setStyle("-fx-base: transparent;");
		botStop.setFocusTraversable(false);
		botMute.setStyle("-fx-base: transparent;");
		botMute.setFocusTraversable(false);
		botPro.setStyle("-fx-base: transparent;");
		botPro.setFocusTraversable(false);
		botAnt.setStyle("-fx-base: transparent;");
		botAnt.setFocusTraversable(false);

		botPlay.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent evento){
				i.stop();

				if(state != PLAYING){
					f = new File(diretorio+((Mp3)playlist.getSelectionModel().getSelectedItem()).getNome());

					i.play(f);
					i.set_volume((float)volume.getValue());
					String t = ((Mp3)playlist.getSelectionModel().getSelectedItem()).getTempo();
					int index = 0;
					for( ; t.charAt(index) != ':' ; index++ ){}
					tempo = Double.parseDouble(t.substring(0, index)) * 60 + Double.parseDouble(t.substring(index+1)) ;
					thread = true;
					reproducao.setMax(tempo);
					System.out.println("TEMPO GUI"+tempo);
					tre = true;
					RpprMain as = new RpprMain();			
					botPlay.setGraphic(new ImageView(ipause));					
					state = PLAYING;					


					//as.run();
					//repro.run();
					//while(index < 10){
					//a.set(i.Microseconds());
					//a.set(i.Microseconds());
					//index++;
					//}
					//System.out.println(i.get_minimo());
					//while(i.get_maximo()==i.get_minimo()){}
				}else{
					i.pause();
					state = PAUSED;
					botPlay.setGraphic(new ImageView(iplay));
				}
			}
		});

		MenuItem mescu = new MenuItem("Menos Escutadas");
		MenuItem Top = new MenuItem("Top 10");

		Top.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent evento){
				Gerarplaytop10();
			}
		});

		mescu.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent evento){
				Gerarplaymenosouvidas();
			}
		});
		
		menuCustons.getItems().addAll( Top, mescu );

		MenuBar menuBar = new MenuBar();
		menuBar.getMenus().addAll(menuArtist, menuGen, menuCustons);
		HBox hboxl1 = new HBox();
		hboxl1.getChildren().addAll(botAnt, botPlay, botPro, botStop, botMute);

		vBox.getChildren().addAll(menuBar, hboxl1, playlist);

		return vBox;

	}
}
