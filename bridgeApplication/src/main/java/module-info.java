module com.lduncan1712._snp {
    requires javafx.controls;
    requires javafx.fxml;
	requires com.google.api.client.auth;
	requires com.google.api.client.json.gson;
	requires google.api.services.sheets.v4.rev581;
	requires com.google.api.client;
	requires google.api.client;
	requires com.google.api.client.extensions.java6.auth;
	requires com.google.api.client.extensions.jetty.auth;
	requires javafx.graphics;
	requires java.sql;
	requires javafx.base;
	requires java.xml;

    opens com.lduncan1712.bridgeApplication.controllers to javafx.fxml;
    exports com.lduncan1712.bridgeApplication;
}
