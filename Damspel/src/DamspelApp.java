import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DamspelApp extends Application implements EventHandler<ActionEvent> {
	
	private Damspel spel = new Damspel();
	private Label[] labels = new Label[4];
	private Button[] buttons = new Button[101];
	private CheckBox cb1 = new CheckBox("Bord roteren"), cb2 = new CheckBox("Zetten bevestigen");

	public void start(Stage stage) {
		
		// Scene met daarin borderpane
		BorderPane root = new BorderPane();
		Scene scene = new Scene(root, 563, 432);

		// vbox geset in left borderpane
		VBox vbLeft = new VBox(), vbTop = new VBox();
		root.setLeft(vbLeft);
		root.setTop(vbTop);
		root.setPadding(new Insets(0, 5, 5, 8));
		vbLeft.setPrefSize(150, 350);
		vbLeft.setSpacing(10);
		vbTop.setAlignment(Pos.CENTER_RIGHT);
		vbTop.setPadding(new Insets(5, 0, 5, 0));
		// gridpane geset in center borderpane
		GridPane gp = new GridPane();
		root.setCenter(gp);

		// labels aanmaken
		for (int i = 0; i < labels.length; i++) {
			labels[i] = new Label();
		}
		
		// buttons aanmaken
		for (int i = 0; i < buttons.length-1; i++) {
			buttons[i] = new Button(/*Integer.toString(i)*/);
			buttons[i].setPrefSize(40, 40);
			gp.add(buttons[i], i%10, i/10);
			buttons[i].setOnAction(this);
		}
		
		cb2.setOnAction(this);
		vbTop.getChildren().add(labels[0]);
		buttons[buttons.length-1] = new Button("Reset");
		buttons[buttons.length-1].setPrefWidth(120);
		vbLeft.getChildren().addAll(buttons[100], cb1, cb2, labels[1],labels[2],labels[3]);
		labels[0].setTextFill(Color.RED);
		labels[0].setText("meldingen komen hier!");
		labels[1].setText("Speler: ");
		labels[2].setText("Stenen Z: ");
		labels[3].setText("Stenen W: ");
		buttons[100].setOnAction(this);
		scene.getStylesheets().add("CSS/stylesheet.css");
		stage.setTitle("Dammen!");
		stage.setScene(scene);
		stage.show();
		scene.setOnKeyReleased(e -> { 
			if(e.getCode() == KeyCode.ESCAPE) {
				if(spel.vergeefBeurt()) { 
					updateBord(); 
				}
			} 
		});
		
		spel.foutmeldingProperty().addListener(c -> labels[0].setText(spel.getfoutmelding()));
		this.reset();
		this.updateBord();
	}
	
	public void reset()
	{
		for(int i = 0; i < buttons.length-1; i++)
		{
			//je moet eerst alle styleclassen verwijderen, anders heeft een button meerderen en werkt het spel niet meer na resetten.
			buttons[i].getStyleClass().removeAll("ZWART", "WIT", "LEEG", "NIETSPEELBAAR", "ZWARTVAKJE", "WITVAKJE");
			
			//bepaald of het een wit of zwart vakje moet worden.
			boolean check = ((i/10)%2 == 0 && i%2 == 0) || ((i/10)%2 == 1 && i%2 == 1);
			BoardTileStatus status = null;
			if(check && i < 40) status = BoardTileStatus.ZWART;
			else if(check && i > 59) status = BoardTileStatus.WIT;
			else if(check) status = BoardTileStatus.LEEG;
			if(!check) { status = BoardTileStatus.NIETSPEELBAAR; /*buttons[i].setText("X");*/ }
			buttons[i].getStyleClass().add(status == null ? "" : status.name());
			
			//als de array nog leeg is, dus je hebt het spel voor het eerst gerunt, dan moet je toevoegen aan de array, anders moet je index wijzigen
			if(spel.getLength() == 100) spel.setArray(i, status);//als de array niet meer leeg is
			if(spel.getLength() != 100) spel.addArray(status);//als de array nog leeg is
			
			if(check) buttons[i].getStyleClass().add("ZWARTVAKJE");
			else buttons[i].getStyleClass().add("WITVAKJE");
		}
	}

	@Override
	public void handle(ActionEvent event) {
		// TODO Auto-generated method stub
		if(event.getSource() == buttons[100])
		{
			spel.reset();
			this.reset();
			updateBord();
			System.out.println(spel.toString());
		}
		else if(event.getSource() == cb2) spel.flipConfirm();
		else
		{
			for(int i = 0; i < buttons.length-1; i++)
			{
				if(event.getSource().equals(buttons[i]))
				{
					if(spel.isVeldSpeelbaar(i)) 
					{
					    updateBord();
					}
					if (spel.getDoFocus()) 
					{
						boolean useless_assignment = (buttons[i].getStyleClass().contains("FOCUS")) ? buttons[i].getStyleClass().remove("FOCUS") : buttons[i].getStyleClass().add("FOCUS");
						spel.setDoFocus(false);
					}
					System.out.println("veld_clicked; index: " + i);
				}
			}
		}
	}

	public void updateBord()
	{
		System.out.println(spel.getSpelerObject().getSteenHoeveelheid());

		labels[3].setText("Stenen W: " + spel.getSpecifiekeSpeler(0).getSteenHoeveelheid());
		labels[2].setText("Stenen Z: " + spel.getSpecifiekeSpeler(1).getSteenHoeveelheid());
		labels[1].setText("Speler: " + spel.getSpelerObject().getNaam());
		for(int i = 0; i < buttons.length-1; i++)
		{
			buttons[i].getStyleClass().removeAll("ZWART", "WIT", "FOCUS");
			buttons[i].getStyleClass().add(spel.getArray(i).name());
		}
	}
	public static void main(String[] args) {
		Application.launch(args);
	}

}
