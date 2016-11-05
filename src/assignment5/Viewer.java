/* CRITTERS Viewer.java
 * EE422C Project 5 submission by
 * Yilin Zhu
 * yz22778
 * 16450
 * Andrew Wong
 * aw27772
 * 16450
 * Slip days used: <0>
 * Fall 2016
 */
package assignment5;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.reflections.Reflections;

import assignment5.Critter.CritterShape;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Shape;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;

public class Viewer extends Application {
	final int worldX = 650;
	final int worldY = 650;
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
	Spinner<Integer> seedSpin;
	Button seedBtn;
	Separator sep4;
	Spinner<Integer> runSpin;
	Button runBtn;
	Separator sep5;
	Button clearBtn;
	Separator sep6;
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
		// grid.setPadding(new Insets(0, 0, 0, 0));

		boardGroup = new StackPane();
		boardGroup.setId("stack-pane");

		worldBoard = new Rectangle(worldX, worldY);
		worldBoard.setId("world-board");
		boardGroup.getChildren().add(worldBoard);
		// worldBoard.setX(300);
		// worldBoard.setY(300);
		// StackPane.setMargin(worldBoard, new Insets(0, 0, 0, 0));
		grid.add(boardGroup, 0, 0);

		critterLbl = new Label("Critter World");
		makeCbox = new ComboBox<String>();
		makeCbox.setId("make-combo");

		makeSpin = new Spinner<Integer>(1, Integer.MAX_VALUE, 1);
		// makeSpin.setEditable(true);
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
		seedSpin = new Spinner<Integer>(1, Integer.MAX_VALUE, 1);
		seedBtn = new Button("SEED");
		sep4 = new Separator();
		runSpin = new Spinner<Integer>(1, Integer.MAX_VALUE, 1);
		runBtn = new Button("RUN");
		sep5 = new Separator();
		clearBtn = new Button("CLEAR");
		sep6 = new Separator();
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
		controlPanel.getChildren().add(seedSpin);
		controlPanel.getChildren().add(seedBtn);
		controlPanel.getChildren().add(sep4);
		controlPanel.getChildren().add(runSpin);
		controlPanel.getChildren().add(runBtn);
		controlPanel.getChildren().add(sep5);
		controlPanel.getChildren().add(clearBtn);
		controlPanel.getChildren().add(sep6);
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
		for (Class<? extends Critter> item : subTypes) {
			s = item.getName();
			if (Controller.isCritter(s)) {
				validCritters.add(s.substring(s.lastIndexOf('.') + 1));
			}
		}
		myPackage = s.substring(0, s.lastIndexOf('.'));
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
				// statConsoleArea.appendText("step " + step + '\n');
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

		seedBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				statConsoleArea.appendText(Controller.seed(seedSpin.getValue()));
			}
		});

		runBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (!inRunState) {
					for (Node n : controlPanel.getChildren())
						n.setDisable(true);
					critterLbl.setDisable(false);
					runBtn.setDisable(false);
					runBtn.setText("STOP");
					quitBtn.setDisable(false);

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
					for (Node n : controlPanel.getChildren())
						n.setDisable(false);
					runBtn.setText("RUN");
					timer.cancel();
					inRunState = false;
				}
			}
		});

		clearBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				draw(Controller.clear());
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
		List<Shape> critters = new ArrayList<Shape>();
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				if (m.containsKey(new Point(i, j))) {
					ArrayList<Critter> critArr = m.get(new Point(i, j));
					if (critArr.isEmpty())
						continue;
					Critter crit = critArr.get(critArr.size() - 1);
					double s = Math.min((worldX - worldPadding * 2) / (w - 1), (worldY - worldPadding * 2) / (h - 1))
							/ 8;
					double x = (worldX - worldPadding * 2.0 - s) * i / (w - 1) + worldPadding;
					double y = (worldY - worldPadding * 2.0 - s) * j / (h - 1) + worldPadding;

					// get shape specific to critter
					CritterShape switchShape = crit.viewShape();
					Shape a;

					switch (switchShape) {
					case CIRCLE:
						a = new Circle(s);
						break;
					case SQUARE:
						a = new Rectangle();
						((Rectangle) a).setWidth(1.6 * s);
						((Rectangle) a).setHeight(1.6 * s);
						((Rectangle) a).setArcWidth(s/2);
						((Rectangle) a).setArcHeight(s/2);
						break;
					case TRIANGLE:
						// create a equilateral triangle
						a = new Polygon();
						// coordinates to create triangle
						double len =  s/2*Math.sqrt(3);
						((Polygon) a).getPoints().addAll(new Double[] { len,
								0.0, 0.0, len*Math.sqrt(3), len*2, len*Math.sqrt(3)});
						break;
					case DIAMOND:
						// create a rhombus
						a = new Rectangle();
						((Rectangle) a).setWidth(s * 1.6);
						((Rectangle) a).setHeight(s * 1.6);
						((Rectangle) a).setArcWidth(s/2);
						((Rectangle) a).setArcHeight(s/2);
						((Rectangle) a).setRotate(45);
						break;
					case STAR:
						// create a 5-point star
						a = new Polygon();
						// coordinates to create star
						Double[] coord = new Double[20];
						double rho = 1.052;
						double R = 0.402;
						for (int k = 0; k < 5; k++){
							coord[4*k] = s*rho*(Math.sin(72*k*Math.PI/180)+1);
							coord[4*k+1] = s*rho*(-Math.cos(72*k*Math.PI/180)+1);
							coord[4*k+2] = s*(R*Math.sin((72*k+36)*Math.PI/180)+rho);
							coord[4*k+3] = s*(-R*Math.cos((72*k+36)*Math.PI/180)+rho);
						}
						((Polygon) a).getPoints().addAll(coord);
						break;
					default:
						a = new Circle(s);
						break;

					}
					a.setFill(crit.viewFillColor());
					a.setStroke(crit.viewOutlineColor());
					critters.add(a);
					// translate wrt top-left corner of circle
					a.setTranslateX(x - s / 2);
					a.setTranslateY(y - s / 2);
				}
			}
		}
		boardGroup.getChildren().addAll(critters);
	}

	public static void init(String[] args) {
		launch(args);
	}

}
