

import com.sun.prism.j2d.paint.MultipleGradientPaint;
import javafx.application.Application;
import javafx.event.*;
import javafx.scene.*;
import javafx.scene.canvas.*;
import javafx.scene.paint.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.application.Platform;
import javafx.geometry.*;
import javafx.scene.canvas.Canvas.*;
import javafx.scene.text.*;
import javafx.animation.*;
/**
 *
 * @author Who
 */
abstract class CanvasPane extends Pane {
   Canvas canvas;
   boolean bUpdating;

   public CanvasPane() {
      this(128, 64);
   }

   public CanvasPane(double w, double h) {
      super();

      this.layoutBoundsProperty().addListener(new ChangeListener<Bounds>() {
         @Override
         public void changed(ObservableValue<? extends Bounds> observable, Bounds oldValue, Bounds newValue) {
            if (!bUpdating) {
               bUpdating = true;
               Platform.runLater(() -> {
                  double w4 = getPane().getWidth();
                  //  w4+=16-(w4%16);
                  canvas.setWidth(w4);
                  w4=getPane().getHeight();
                  canvas.setHeight(w4);

                  init();
                  repaint();
                  bUpdating = false;
               });
            }
         }});
      canvas = new Canvas(w, h);

      getChildren().add(canvas);
   }

   public Pane getPane() {
      return this;
   }

   abstract public void repaint();
   abstract public void init();

}


public class GraphingCalculator extends Application {
    float t;
    AnimationTimer timer;
    tree leafy=new tree();
    @Override
    public void start(Stage stage) {

       CanvasPane pane= new CanvasPane(){
           public void init(){
               GraphicsContext ctx=canvas.getGraphicsContext2D();
               
               
               ctx.setFill(new Color(0,0,0,0.5));
               ctx.fillRoundRect(10,500,600,48,10,10); 
               ctx.setFill(Color.WHITE);
               ctx.setFont(new Font("MonoType Corsiva",42));
               ctx.fillText("Graphing Calc", 170, 540);
               ctx.beginPath();
               ctx.moveTo(10,10);
               ctx.lineTo(590,10);
               ctx.lineTo(590,410);
               ctx.lineTo(10,410);
               ctx.closePath();
               ctx.clip();
           }
           public void repaint(){
               GraphicsContext ctx=canvas.getGraphicsContext2D();
               
               ctx.setFill(Color.BLACK);
               ctx.fillRoundRect(10,10, 580,400,20,20);// int x, int y, int width, int height, int arc width, int arc height
               ctx.setStroke(Color.AQUA);
               
               
               ctx.beginPath();
               ctx.moveTo(10,200);
               ctx.lineTo(590,200);
               ctx.stroke();
               ctx.moveTo(300,10);
               ctx.lineTo(300,410);
               ctx.stroke();
               //hashes
               
               for(int i=0; i<8; i++){
               ctx.moveTo(295,80+40*i);
               ctx.lineTo(305,80+40*i);
               ctx.stroke();
               }//x axis dashes
               for(int i=0; i<15; i++){
               ctx.moveTo(20+40*i,195);
               ctx.lineTo(20+40*i,205);
               ctx.stroke();
               }//y axis dashes
               

               
               
               
               ctx.setStroke(Color.RED);
               ctx.beginPath();
               
               float x=-7.5f;
               float y=leafy.calc(x);
               ctx.moveTo(300+40*x+t,200-40*y);//x+t
               while(x<7.5f){
                   x+=1/40.0f;
                   y=leafy.calc(x+t);
                   ctx.lineTo(300+40*x,200-40*y);
               }
               ctx.stroke();
           }
       };
       leafy.parse("0");
       
       
       timer=new AnimationTimer(){
           public void handle(long now){
               t+=.1f;
               if(t>12) t-=24;
               pane.repaint();
           }
       };
       timer.start();
        StackPane root = new StackPane();
        root.setOnMouseClicked(e->{
           double y=e.getSceneY();
           if(y>500&&y<540)Platform.exit();
        });
        root.getChildren().add(pane);
        GridPane gp=new GridPane();
        
        gp.setPadding(new Insets(600,100,120,100));
        Font fnt=new Font("Segoe UI",45);
        TextField tf= new TextField();
        
        tf.setPrefWidth(300);
        tf.setOnAction(e->{
            leafy.parse(tf.getText());
            pane.repaint();
        });
        tf.setFont(fnt);
        gp.add(tf, 0,0,4,1);

        String s="789+456-123*±0./c()=";
        for(int i=0; i<s.length(); i++){
        Button btn=new Button(s.charAt(i)+"");
        btn.setStyle(" -fx-background-color: \n" +
"        rgba(0,0,0,0.08),\n" +
"        linear-gradient(#5a61af, #51536d),\n" +
"        linear-gradient(#e4fbff 0%,#cee6fb 10%, #a5d3fb 50%, #88c6fb 51%, #d5faff 100%);\n" +
"    -fx-background-insets: 0 0 -1 0,0,1;\n" +
"    -fx-background-radius: 5,5,4;\n" +
"    -fx-padding: 3 30 3 30;\n" +
"    -fx-text-fill: #242d35;\n");
        btn.setFont(fnt);
        btn.setMinWidth(120);
        gp.add(btn,(i%4),i/4+1);
        btn.setId(s.charAt(i)+"");
        btn.setOnAction(new EventHandler<ActionEvent>(){

             @Override
                     
                    public void handle(ActionEvent event2)
                    {   
                        if(((Button)event2.getSource()).getId().compareTo("c")==0){
                            tf.setText("");
                            leafy.parse("0");
                            pane.repaint();
                        }
                        
                         else if(((Button)event2.getSource()).getId().compareTo("±")==0){
                             String output=tf.getText();
                             tf.setText("-"+"("+output+")");
                         }
                       else if(((Button)event2.getSource()).getId().compareTo("=")==0){
                           leafy.save();
                           leafy.parse(tf.getText());
                           float z=leafy.calc(0);
                           tf.setText(String.valueOf(z));
                           leafy.restore();
                           
                        }
                        
                        else{
                        String output=tf.getText();
                        output+=((Button)event2.getSource()).getId();
                        tf.setText(output);
                        }
                    }  
        }
        );   
        }
        

        root.getChildren().add(gp);
        root.setStyle("-fx-background-color:#00000000");
        Scene scene = new Scene(root, 610, 1024);
        stage.setX(0);
        stage.setY(40);
        Paint pt=new LinearGradient(0,0,100,80,false,CycleMethod.REFLECT,new Stop(0,Color.LIGHTBLUE),
                new Stop(1,Color.WHITE));
        scene.setFill(pt);
        stage.setTitle("Graphing Calculator");
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.show();
    }
   

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}