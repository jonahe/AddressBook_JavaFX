package com.jonahe.addressbook.app;

import java.io.File;
import java.net.MalformedURLException;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;

public class CustomListCell extends ListCell<AddressBookEntry> {

	private final ImageView imageView;
	private final File maleImagePath;
	private final File femaleImagePath;

	public CustomListCell(File maleImagePath, File femaleImagePath) {
		this.maleImagePath = maleImagePath;
		this.femaleImagePath = femaleImagePath;
		imageView = new ImageView();
		System.out.println(maleImagePath.exists());


		imageView.setFitHeight(70);
		imageView.setFitWidth(70);
		imageView.setPreserveRatio(true);
	}



	@Override
	protected void updateItem(AddressBookEntry entry, boolean empty) {
		super.updateItem(entry, empty);
		if(empty) {
			setText(null);
			setGraphic(null);
		} else {
			Person person = entry.getPerson();
			File imagePath = person.getGender() == Gender.MALE ? maleImagePath : femaleImagePath;
			try {
				imageView.setImage(new Image(imagePath.toURI().toURL().toString(), true));
			} catch (MalformedURLException e) {
				System.out.println("Something went wrong in loadig img url");
				e.printStackTrace();
			}


			// gradient
			RadialGradient shadePaint = new RadialGradient(
					0, 0, 0.5, 0.5, 1, true, CycleMethod.NO_CYCLE,
					new Stop(1, Color.LIGHTBLUE),
					new Stop(0, Color.TRANSPARENT)
					);


			HBox cellGraphic = new HBox();
			cellGraphic.setBackground(new Background(new BackgroundFill(shadePaint, null, new Insets(-10))));
			VBox vboxPersonDetails = new VBox(new Label(person.getFullName()), new Label(entry.getContactInfo().getPhoneNumber()));
			vboxPersonDetails.setPrefWidth(150);
			VBox optionButtons = new VBox(new Button("Edit"), new Button("Delete"));
			optionButtons.setAlignment(Pos.TOP_CENTER);
			
			cellGraphic.getChildren().addAll(imageView, vboxPersonDetails, optionButtons);
			setGraphic(cellGraphic);
		}
	}
}
