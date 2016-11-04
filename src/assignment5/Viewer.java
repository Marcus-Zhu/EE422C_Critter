package assignment5;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.reflections.Reflections;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.paint.Color;

public class Viewer extends Application {
	final int worldX = 550;
	final int worldY = 550;
	final int worldPadding = 50;

	GridPane grid;
	StackPane boardGroup;
	Rectangle worldBoard;
	Label critterLbl;
	ComboBox<String> makeCbox;
	Spinner<Integer> makeSpin;
	HBox makeBox;
	Button makeBtn;
	Separator sep1;
	Spinner<Integer> stepSpin;
	Button stepBtn;
	Separator sep2;
	ComboBox<String> statCbox;
	Button statBtn;
	Separator sep3;
	Spinner<Integer> runSpin;
	Button runBtn;
	Separator sep4;
	Button quitBtn;
	VBox controlPanel;
	TextArea statConsoleArea;
	boolean inRunState = false;
	Timer timer;
	List<String> validCritters;
	String myPackage;

	@Override
	public void start(Stage primaryStage) {
		grid = new GridPane();
		grid.getStyleClass().add("grid");
//		grid.setPadding(new Insets(0, 0, 0, 0));

		boardGroup = new StackPane();
		boardGroup.setId("stack-pane");

		worldBoard = new Rectangle(worldX, worldY);
		worldBoard.setId("world-board");
		boardGroup.getChildren().add(worldBoard);
//		worldBoard.setX(300);
//		worldBoard.setY(300);
//	    StackPane.setMargin(worldBoard, new Insets(0, 0, 0, 0));
		grid.add(boardGroup, 0, 0);

		critterLbl = new Label("Critter World");
		makeCbox = new ComboBox<String>();
		makeCbox.setId("make-combo");
		makeSpin = new Spinner<Integer>(1, Integer.MAX_VALUE, 1);
		makeSpin.setId("make-spin");
		makeBtn = new Button("MAKE");
		makeBox = new HBox();
		makeBox.getStyleClass().add("hbox");
		makeBox.getChildren().add(makeCbox);
		makeBox.getChildren().add(makeSpin);
		sep1 = new Separator();
		stepSpin = new Spinner<Integer>(1, Integer.MAX_VALUE, 1);
		stepBtn = new Button("STEP");
		sep2 = new Separator();
		statCbox = new ComboBox<String>();
		statBtn = new Button("STAT");
		sep3 = new Separator();
		runSpin = new Spinner<Integer>(1, Integer.MAX_VALUE, 1);
		runBtn = new Button("RUN");
		sep4 = new Separator();
		quitBtn = new Button("QUIT");

		controlPanel = new VBox();
		controlPanel.getStyleClass().add("vbox");
		controlPanel.getChildren().add(critterLbl);
		controlPanel.getChildren().add(makeBox);
		controlPanel.getChildren().add(makeBtn);
		controlPanel.getChildren().add(sep1);
		controlPanel.getChildren().add(stepSpin);
		controlPanel.getChildren().add(stepBtn);
		controlPanel.getChildren().add(sep2);
		controlPanel.getChildren().add(statCbox);
		controlPanel.getChildren().add(statBtn);
		controlPanel.getChildren().add(sep3);
		controlPanel.getChildren().add(runSpin);
		controlPanel.getChildren().add(runBtn);
		controlPanel.getChildren().add(sep4);
		controlPanel.getChildren().add(quitBtn);
		grid.add(controlPanel, 1, 0);

		statConsoleArea = new TextArea("Critter Stats Console:\n");
		statConsoleArea.setDisable(true);
		grid.add(statConsoleArea, 0, 1, 2, 1);

		setActions();

		// get all the names of class<? extends Critter>
		Reflections reflections = new Reflections("assignment5");

		Set<Class<? extends Critter>> subTypes = reflections.getSubTypesOf(Critter.class);
		String s = new String();
		validCritters = new ArrayList<String>();
		for (Class<? extends Critter> item: subTypes){
			s = item.getName();
			if (Controller.isCritter(s)) {
				validCritters.add(s.substring(s.lastIndexOf('.')+1));
			}
		}
		myPackage = s.substring(0,s.lastIndexOf('.'));
		Controller.clear();

		Collections.sort(validCritters);

		// output names to combobox
		makeCbox.getItems().addAll(validCritters);
		statCbox.getItems().addAll(validCritters);

		Scene scene = new Scene(grid);

		primaryStage.setScene(scene);
		scene.getStylesheets().add("style.css");
		primaryStage.setTitle("Critter World");
		primaryStage.setResizable(false);
		primaryStage.show();
	}

	private void setActions() {

		makeBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				makeCbox.setDisable(true);
				makeSpin.setDisable(true);
				statConsoleArea.appendText(Controller.make(myPackage + '.' + makeCbox.getValue(), makeSpin.getValue()));
				Platform.runLater(new Runnable() {
					public void run() {
						draw(Controller.show());
					}
				});
				makeCbox.setDisable(false);
				makeSpin.setDisable(false);
			}
		});

		stepBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				stepSpin.setDisable(true);
				int step = stepSpin.getValue();
//				statConsoleArea.appendText("step " + step + '\n');
				Platform.runLater(new Runnable() {
					public void run() {
						draw(Controller.step(step));
					}
				});
				stepSpin.setDisable(false);
			}
		});

		statBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				statCbox.setDisable(true);
				String critName = statCbox.getValue();
				statConsoleArea.appendText(Controller.stats(myPackage + '.' + critName));
				statCbox.setDisable(false);
			}
		});

		runBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (!inRunState) {
					runSpin.setDisable(true);
					runBtn.setText("STOP");
					int rate = runSpin.getValue();
					timer = new Timer();
					timer.scheduleAtFixedRate(new TimerTask() {
						public void run() {
							Platform.runLater(new Runnable() {
								public void run() {
									draw(Controller.step(1));
								}
							});
						}
					}, 0, 1000L / (long) rate);
					inRunState = true;
				} else {
					runSpin.setDisable(false);
					runBtn.setText("RUN");
					timer.cancel();
					inRunState = false;
				}
			}
		});

		quitBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				((Node) (e.getSource())).getScene().getWindow().hide();
			}
		});

		statConsoleArea.textProperty().addListener(new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue<?> observable, Object oldValue, Object newValue) {
				statConsoleArea.setScrollTop(Double.MAX_VALUE);
			}
		});
	}

	private void draw(Map<Point, ArrayList<Critter>> m) {
		int w = Params.world_width;
		int h = Params.world_height;
		boardGroup.getChildren().clear();
		boardGroup.getChildren().add(worldBoard);
		List<Circle> critters = new ArrayList<Circle>();
		for (int i = 0; i < w; i++){
			for (int j = 0; j < h; j++){
				if (m.containsKey(new Point(i,j))){
					ArrayList<Critter> critArr = m.get(new Point(i,j));
					Critter crit = null;
					if (critArr.size() > 0 && critArr != null )
						crit = critArr.get(critArr.size() - 1);
					
					double s = Math.min((worldX-worldPadding*2)/(w-1),
							(worldY-worldPadding*2)/(h-1))/10;
					double x = (worldX-worldPadding*2.0-s)*i/(w-1)+worldPadding;
					double y = (worldY-worldPadding*2.0-s)*j/(h-1)+worldPadding;
//					System.out.println(x+" "+y);
					Circle a = new Circle(s);
					a.setFill(crit.viewColor());
					critters.add(a);
					// translate wrt top-left corner of circle
					a.setTranslateX(x-s/2);
					a.setTranslateY(y-s/2);
				}
			}
		}
		boardGroup.getChildren().addAll(critters);
	}

	public static void main(String[] args) {
		launch(args);
	}

}
