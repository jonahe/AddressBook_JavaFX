package com.jonahe.addressbook.app;

import java.io.File;
import java.net.MalformedURLException;

import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.Paint;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;

public class CustomListCell extends ListCell<AddressBookEntry> {

	private final Image maleImage;
	private final Image femaleImage;
	private final Image editImage;
	private final Image addContactImage;
	private final Image removeContactImage;
	
	private final  ControllerAddressBook controller;
	private final boolean isInMainContactList; // different things should show up depending on which listview we're dealing with
	private final ImageView imageView;
	
	private static Tooltip addTooltip = new Tooltip("Add as contact connection");
	private static Tooltip removeTooltip = new Tooltip("Remove from contact connections");
	private static Tooltip editTooltip = new Tooltip("Edit contact");

	public CustomListCell(	final Image maleImage, 
							final Image femaleImage, 
							final Image editImage,
							final Image addContactImage,
							final Image removeContactImage,
							final ControllerAddressBook controller,
							final boolean isInMainContactList) {
		this.maleImage = maleImage;
		this.femaleImage = femaleImage;
		this.editImage = editImage;
		this.addContactImage = addContactImage;
		this.removeContactImage = removeContactImage;
		this.controller = controller;
		this.isInMainContactList = isInMainContactList;
		
		imageView = new ImageView();


		imageView.setFitHeight(60);
		imageView.setFitWidth(60);
		imageView.setPreserveRatio(true);
	}




	@Override
	protected void updateItem(AddressBookEntry entry, boolean empty) {
		super.updateItem(entry, empty);
		
		// gradient
		RadialGradient shadePaint = new RadialGradient(
				0, 0, 0.5, 0.5, 1, true, CycleMethod.NO_CYCLE,
				new Stop(1, Color.valueOf("#ED5A47")),
				new Stop(0, Color.TRANSPARENT)
				);
		// this.setBackground(new Background(new BackgroundFill(shadePaint, null, new Insets(-10))));
		
		if(empty) {
			setText(null);
			setGraphic(null);
			// this.setBackground(new Background(new BackgroundFill(null, null, null)));
		} else {
			Person person = entry.getPerson();
			Image genderimage = person.getGender() == Gender.MALE ? maleImage : femaleImage;
			imageView.setImage(genderimage);


			HBox cellGraphic = new HBox();
			cellGraphic.setBackground(new Background(new BackgroundFill(shadePaint, null, new Insets(-10))));
			
			VBox vboxPersonDetails = new VBox(new Label(person.getFullName()), new Label(entry.getContactInfo().getPhoneNumber()));
			vboxPersonDetails.setAlignment(Pos.CENTER_LEFT);
			vboxPersonDetails.setPadding(new Insets(0,3,0,5));
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
			optionButtons.setAlignment(Pos.CENTER_RIGHT);
			
			cellGraphic.getChildren().addAll(imageView, vboxPersonDetails, optionButtons);
			setGraphic(cellGraphic);
			
			//this.setEffect(new InnerShadow(2, Color.GRAY)); // only visible on empty rows? 
			this.setPrefHeight(75);
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
		
		// invisible nodes still takes up space. bind visibility to managed property
		editButton.managedProperty().bind(editButton.visibleProperty());
		addButton.managedProperty().bind(addButton.visibleProperty());
		removeButton.managedProperty().bind(removeButton.visibleProperty());
		
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
		Button editButton = new Button();
		final ImageView img = new ImageView();
		img.setFitHeight(25);
		img.setFitWidth(25);
		img.setPreserveRatio(true);
		img.setImage(editImage);

		editButton.setGraphic(img);
		editButton.setTooltip(editTooltip);
		
		return editButton;
	}
	
	private Button createAddButton(){
		Button addButton = new Button();
		final ImageView img = new ImageView();
		img.setFitHeight(25);
		img.setFitWidth(25);
		img.setPreserveRatio(true);
		img.setImage(addContactImage);

		addButton.setGraphic(img);
		//addButton.setBackground(new Background(new BackgroundFill(Paint.valueOf("TRANSPARENT"), null, new Insets(0.0))));
		addButton.setTooltip(addTooltip);
		return addButton;
	}
	
	private Button createRemoveButton(){
		Button removeBtn = new Button();
		final ImageView img = new ImageView();
		img.setFitHeight(25);
		img.setFitWidth(25);
		img.setPreserveRatio(true);
		img.setImage(removeContactImage);

		removeBtn.setGraphic(img);
		removeBtn.setTooltip(removeTooltip);
		
		return removeBtn;
	}
}
