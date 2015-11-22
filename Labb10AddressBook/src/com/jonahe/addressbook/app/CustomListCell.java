package com.jonahe.addressbook.app;

import java.io.File;
import java.net.MalformedURLException;

import javafx.beans.binding.Bindings;
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
	private final File addContactImagePath;
	private final File removeContactImagePath;
	
	private final  ControllerAddressBook controller;
	private final boolean isInMainContactList; // different things should show up depending on which listview we're dealing with
	private final ImageView imageView;

	public CustomListCell(	final File maleImagePath, 
							final File femaleImagePath, 
							final File editImagePath,
							final File addContactImagePath,
							final File removeContactImagePath,
							final ControllerAddressBook controller,
							final boolean isInMainContactList) {
		this.maleImagePath = maleImagePath;
		this.femaleImagePath = femaleImagePath;
		this.editImagePath = editImagePath;
		this.addContactImagePath = addContactImagePath;
		this.removeContactImagePath = removeContactImagePath;
		this.controller = controller;
		this.isInMainContactList = isInMainContactList;
		
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
//			Button editButton = new Button("Edit");
//			try {
//				final ImageView img = new ImageView();
//				img.setFitHeight(20);
//				img.setFitWidth(20);
//				img.setPreserveRatio(true);
//				img.setImage(new Image(editImagePath.toURI().toURL().toString(), true));
//				editButton.setGraphic(img);
//			} catch (MalformedURLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			Button editButton = createEditButton();
			Button addButton = createAddButton();
			Button removeButton = createRemoveButton();
			
			
			bindAddRemoveVisability(editButton, addButton, removeButton);
			
			setButtonListeners(editButton, addButton, removeButton);
			
			VBox optionButtons = new VBox(editButton, addButton, removeButton);
			optionButtons.setAlignment(Pos.TOP_CENTER);
			
			cellGraphic.getChildren().addAll(imageView, vboxPersonDetails, optionButtons);
			setGraphic(cellGraphic);
		}
	}
	
	private void setButtonListeners(Button editButton, Button addButton, Button removeButton) {
		if(isInMainContactList) {
			setEditButtonListener(editButton);
			setAddButtonListener(addButton);
		} else {
			setRemoveButtonListener(removeButton);
			// 
		}
	}



	private void setRemoveButtonListener(Button removeButton) {
		removeButton.setOnAction(event -> {
			controller.listViewConnections.requestFocus();
			controller.listViewConnections.getSelectionModel().select(this.getItem());
			controller.onRemovePersonFromConnections();
			}
		);
	}



	private void setAddButtonListener(Button addButton) {
		addButton.setOnAction(event -> {
			controller.listViewContacts.getSelectionModel().select(this.getItem());
			controller.onAddPersonToConnections();
			}
		);
	}



	private void setEditButtonListener(Button btn) {
		btn.setOnAction(event -> {
			controller.listViewContacts.getSelectionModel().select(this.getItem());
			controller.onEdit();
			}
		);
	}
	
	private void bindAddRemoveVisability(Button editButton, Button addButton, Button removeButton){
		if(isInMainContactList) {
			editButton.visibleProperty().bind(controller.inEditOrCreationModeProperty.not());			
			addButton.visibleProperty().bind(controller.inEditOrCreationModeProperty);
			removeButton.setVisible(false);
		} else {
			removeButton.visibleProperty().bind(controller.inEditOrCreationModeProperty);
			editButton.setVisible(false);
			addButton.setVisible(false);
		}
	}
	
	private Button createEditButton() {
		Button editButton = new Button("Edit");
		final ImageView img = new ImageView();
		img.setFitHeight(20);
		img.setFitWidth(20);
		img.setPreserveRatio(true);
		try {
			img.setImage(new Image(editImagePath.toURI().toURL().toString(), true));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		editButton.setGraphic(img);
		
		return editButton;
	}
	
	private Button createAddButton(){
		Button addButton = new Button();
		final ImageView img = new ImageView();
		img.setFitHeight(20);
		img.setFitWidth(20);
		img.setPreserveRatio(true);
		try {
			img.setImage(new Image(addContactImagePath.toURI().toURL().toString(), true));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		addButton.setGraphic(img);
		
		return addButton;
	}
	
	private Button createRemoveButton(){
		Button removeBtn = new Button();
		final ImageView img = new ImageView();
		img.setFitHeight(20);
		img.setFitWidth(20);
		img.setPreserveRatio(true);
		try {
			img.setImage(new Image(removeContactImagePath.toURI().toURL().toString(), true));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		removeBtn.setGraphic(img);
		
		return removeBtn;
	}
}
