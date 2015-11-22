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

	private final File maleImagePath;
	private final File femaleImagePath;
	private final File editImagePath;
	private final  ControllerAddressBook controller;
	private final ImageView imageView;

	public CustomListCell(final File maleImagePath, final File femaleImagePath, final File editImagePath, final ControllerAddressBook controller) {
		this.maleImagePath = maleImagePath;
		this.femaleImagePath = femaleImagePath;
		this.editImagePath = editImagePath;
		this.controller = controller;
		
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
			Button editButton = new Button("Edit");
			try {
				final ImageView img = new ImageView();
				img.setFitHeight(20);
				img.setFitWidth(20);
				img.setPreserveRatio(true);
				img.setImage(new Image(editImagePath.toURI().toURL().toString(), true));
				editButton.setGraphic(img);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			bindAddRemoveVisability(editButton);
			
			setButtonListener(editButton);
			VBox optionButtons = new VBox(editButton, new Button("Delete"));
			optionButtons.setAlignment(Pos.TOP_CENTER);
			
			cellGraphic.getChildren().addAll(imageView, vboxPersonDetails, optionButtons);
			setGraphic(cellGraphic);
		}
	}
	
	private void setButtonListener(Button btn) {
		btn.setOnAction(event -> {
			controller.listViewContacts.getSelectionModel().select(this.getItem());
			controller.onEdit();
			
			}
		);
	}
	
	private void bindAddRemoveVisability(Button editButton){
		editButton.visibleProperty().bind(controller.inEditOrCreationModeProperty.not());
	}
}
