module ru.nsu.wormsarmagedon {
    requires javafx.controls;
    requires javafx.fxml;


    opens ru.nsu.wormsarmagedon to javafx.fxml;
    exports ru.nsu.wormsarmagedon;
}