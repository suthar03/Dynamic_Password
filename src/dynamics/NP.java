/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dynamics;

import java.awt.Font;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import static java.lang.Thread.sleep;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 *
 * @author Suthar
 */
public class NP extends javax.swing.JFrame {
String Add="D:/DyPass",usr="121";
int sec,min,hr,day,dt,month,year;
    /**
     * Creates new form NP
     */
    public NP(String user) {
        usr=user;
        initComponents();
        //Get saved formate of password
         ////System.out.println(fp+"  ::  "+StringValue(fp)+" :: "+passValue(StringValue(fp)));
        //System.out.println("[<MM><+><yyyy>]"+"  Validity::"+validity("[<MM><+><yyyy>]")+"  ::  "+StringValue("[<MM><+><yyyy>]")+" :: PassValue:: "+passValue(StringValue("[<MM><+><yyyy>]")));
        //System.out.println("[<MM>+<dd>]<ss>"+"  Validity::"+validity("[<MM>+<dd>]<ss>")+"  ::  "+StringValue("[<MM>+<dd>]<ss>")+" :: "+passValue(StringValue("[<MM>+<dd>]<ss>")));
        //int opo= Integer.valueOf("2+(5*8)");
        //passf1.setText(passValue(StringValue("[<MM>+<dd>]<ss>")));
        Date dt = new Date();
        crtm.setText(dt.toInstant().toString());
        ////System.out.println(dt.toString());
        clock();
       
    }
    public String CodeValue(String str){
        String res;
        Calendar cal = new GregorianCalendar();
    day = cal.get(Calendar.DAY_OF_WEEK);
    dt = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);
        
        sec = cal.get(Calendar.SECOND);
        min = cal.get(Calendar.MINUTE);
        hr = cal.get(Calendar.HOUR);
         if(str.compareToIgnoreCase("MM")==0)
                        res=String.format("%02d", ++month);
                        else{
                            if(str.compareToIgnoreCase("yyyy")==0)
                                res=String.format("%04d", year);
                            else{
                                if(str.compareToIgnoreCase("dd")==0)
                                    res=String.format("%02d", dt);
                                else{
                                    if(str.compareToIgnoreCase("hr")==0)
                                        res=String.format("%02d", hr);
                                    else{
                                        if(str.compareToIgnoreCase("min")==0)
                                            res=String.format("%02d", min);
                                        else{
                                            if(str.compareToIgnoreCase("ss")==0)
                                                res=String.format("%02d", sec);
                                            else{
                                                if(str.compareToIgnoreCase("day")==0)
                                                res=DayStr(day);
                                            else{
                                                res=str;
                                            }
                                            }
                                        }
                                    }
                                }
                            }
                        }
        return res;
    }
    public String StringValue(String form){
        String res="",shortcd="";
       for(int i=0;i<form.length();++i){
        if(form.charAt(i)=='<'||form.charAt(i)=='>'){
            if(i!=0){
                //Go for codevalue
                res+=CodeValue(shortcd);
                shortcd="";
            }
            
        }else{
            shortcd+=form.charAt(i);
        }   
       }
       res+=CodeValue(shortcd);
       //System.out.println("String valued::"+res);
        return res;
    }
    public String  passValue(String intstr){
        //System.out.println("String Int Value passed:"+intstr);
        String res="",curr="";int br=-1;
      for(int i=0;i<intstr.length();++i){
          if(br==0&&i!=0){
              //eval miust be calcutated
              System.out.println("Going for eval::"+curr);
              res+=String.valueOf(eval(curr));
              curr="";
                      br=-1;--i; continue;
          }else{
              if(intstr.charAt(i)=='['){ br=1;continue;}
              else{
                  if(intstr.charAt(i)==']') {br=0; continue;}
              }
          if(br==-1){
          res+=intstr.charAt(i);
          continue;
          }
          
          }
          curr+=intstr.charAt(i);
      }  
      System.out.println("Just Before::"+res);
      if(br==0){
              //eval miust be calcutated
              System.out.println("Going for eval::"+curr);
              res+=String.valueOf(eval(curr));
              curr="";
                      br=-1;
          }else{
          res=res+curr;
      }
        return res;
    }
    public  int eval( String str) {
    return new Object() {
        int pos = -1, ch;

        void nextChar() {
            ch = (++pos < str.length()) ? str.charAt(pos) : -1;
        }

        boolean eat(int charToEat) {
            while (ch == ' ') nextChar();
            if (ch == charToEat) {
                nextChar();
                return true;
            }
            return false;
        }

        int parse() {
            nextChar();
            int x =  parseExpression();
            if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char)ch);
            return x;
        }

        // Grammar:
        // expression = term | expression `+` term | expression `-` term
        // term = factor | term `*` factor | term `/` factor
        // factor = `+` factor | `-` factor | `(` expression `)`
        //        | number | functionName factor | factor `^` factor

        int parseExpression() {
            int x = parseTerm();
            for (;;) {
                if      (eat('+')) x += parseTerm(); // addition
                else if (eat('-')) x -= parseTerm(); // subtraction
                else return x;
            }
        }

        int parseTerm() {
            int x = parseFactor();
            for (;;) {
                if      (eat('*')) x *= parseFactor(); // multiplication
                else if (eat('/')) x /= parseFactor(); // division
                else return x;
            }
        }

        int parseFactor() {
            if (eat('+')) return parseFactor(); // unary plus
            if (eat('-')) return -parseFactor(); // unary minus

            int x;
            int startPos = this.pos;
            if (eat('(')) { // parentheses
                x = parseExpression();
                eat(')');
            } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                x = Integer.parseInt(str.substring(startPos, this.pos));
            } else  {
                throw new RuntimeException("Unexpected: " + (char)ch);
            }

            if (eat('^')) x = (int) Math.pow(x, parseFactor()); // exponentiation

            return x;
        }
    }.parse();
}
public String encoderr(String gh){
    String cp="";
    Calendar cal = new GregorianCalendar();
    day = cal.get(Calendar.DAY_OF_WEEK);
    dt = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);
        
        sec = cal.get(Calendar.SECOND);
        min = cal.get(Calendar.MINUTE);
        hr = cal.get(Calendar.HOUR);
        for(int i=0;i<gh.length();++i){
            if(gh.charAt(i)=='<'){ //Something going to start
                String str="";
                for(++i;i<gh.length();++i){
                    if(gh.charAt(i)=='>'){   
                        if(str.compareToIgnoreCase("MM")==0)
                        cp+=String.valueOf(month);
                        else{
                            if(str.compareToIgnoreCase("yyyy")==0)
                                cp+=String.valueOf(year);
                            else{
                                if(str.compareToIgnoreCase("dd")==0)
                                    cp+=String.valueOf(dt);
                                else{
                                    if(str.compareToIgnoreCase("hr")==0)
                                        cp+=String.valueOf(hr);
                                    else{
                                        if(str.compareToIgnoreCase("min")==0)
                                            cp+=String.valueOf(min);
                                        else{
                                            if(str.compareToIgnoreCase("ss")==0)
                                                cp+=String.valueOf(sec);
                                        }
                                    }
                                }
                            }
                        }
                        break;
                    
                    }
                    else{
                        str+=gh.charAt(i);
                    }
                }
            }else{
               cp+=gh.charAt(i);
            }
        }
    //passf1.setText(day+"::"+dt+"/"+month+"/"+year+"::"+hr+":"+min+":"+sec);
    return cp;
}
public String DayStr(int i){
    String DDD="";
    switch(i){
        case 1: DDD="SUN"; break;
        case 2: DDD="MON"; break;
        case 3: DDD="TUE"; break;
        case 4: DDD="WED"; break;
        case 5: DDD="THU"; break;
        case 6: DDD="FRI"; break;
        case 7: DDD="SAT"; break;
            
    }
    return DDD;
}
public void clock(){
    Thread clock = new Thread(){
        public void run()
            {
            try {
                for(;;){
                Calendar cal = new GregorianCalendar();
        day = cal.get(Calendar.DAY_OF_WEEK);
                dt = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);
        
        sec = cal.get(Calendar.SECOND);
        min = cal.get(Calendar.MINUTE);
        hr = cal.get(Calendar.HOUR);
        crtm.setText(DayStr(day)+"::"+String.format("%02d", dt)+"/"+String.format("%02d", ++month)+"/"+String.format("%02d", year)+"::"+String.format("%02d", hr)+":"+String.format("%02d", min)+":"+String.format("%02d", sec));
        ////System.out.println(dt+"/"+month+"/"+year+"::"+hr+":"+min+":"+sec);
        
                sleep(1000);
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(NP.class.getName()).log(Level.SEVERE, null, ex);
            }
            }
    };
    clock.start();
       
    
}
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        passf1 = new javax.swing.JTextField();
        passf2 = new javax.swing.JTextField();
        passckr = new javax.swing.JPasswordField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        save = new javax.swing.JButton();
        clear = new javax.swing.JButton();
        cancel = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        tmlbl = new javax.swing.JLabel();
        crtm = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        passf1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                passf1FocusLost(evt);
            }
        });
        passf1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                passf1KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                passf1KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                passf1KeyTyped(evt);
            }
        });

        passf2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                passf2KeyPressed(evt);
            }
        });

        passckr.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                passckrFocusGained(evt);
            }
        });
        passckr.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                passckrKeyPressed(evt);
            }
        });

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel1.setText("Formate for password:");

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel2.setText("Formate for password:");

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel3.setText("Enter Password for current instant:");

        save.setText("Save");
        save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveActionPerformed(evt);
            }
        });

        clear.setText("Clear");
        clear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearActionPerformed(evt);
            }
        });

        cancel.setText("Cancel");
        cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelActionPerformed(evt);
            }
        });

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Hint");
        jLabel4.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        tmlbl.setText("Time and Date:");

        crtm.setFont(new java.awt.Font("DialogInput", 1, 18)); // NOI18N
        crtm.setText("Wed::01/01/2021::10:10:10");

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Dynamic Things");

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jTextArea1.setText("<yyyy> : Year i.e. 2020\n<MM>   : Month i.e.  12\n<dd>   : Date i.e.   30\n<day>  : Day   i.e. WED\n<hr>   : hours  i.e  02\n<min>  : Minutes i.e 31\n<sec>  : Second i.e. 55\n!!For mathematical ope-\nration use [] and Symb-\nols are :\n<+>    : Addition\n<->    : Substraction\n<*>    : Multiplication");
        jScrollPane1.setViewportView(jTextArea1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 148, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(tmlbl)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(crtm))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(15, 15, 15)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(cancel)
                                .addGap(80, 80, 80)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(clear)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 80, Short.MAX_VALUE)
                                .addComponent(save))
                            .addComponent(passf2, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(passckr, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(passf1))
                        .addGap(26, 26, 26)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(tmlbl, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
                    .addComponent(crtm, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(47, 47, 47)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(passf1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addGap(30, 30, 30)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(passf2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2)
                            .addComponent(jLabel4))
                        .addGap(37, 37, 37)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(passckr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3)))
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(save)
                    .addComponent(clear)
                    .addComponent(cancel))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelActionPerformed
        this.dispose();
    }//GEN-LAST:event_cancelActionPerformed

    private void clearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearActionPerformed
        passf1.setText("");
        passf2.setText("");
        passckr.setText("");
        passf1.setEditable(true);
        passf2.setEditable(true);
    }//GEN-LAST:event_clearActionPerformed

    private void passf1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_passf1KeyReleased
    /*    String str = passf1.getText();
        String lst = "";
        for(int i=str.length()-1;i>=0;--i){
               if(str.charAt(i)=='>') break;
               else
                lst=str.charAt(i)+lst;
        }
        //System.out.println("Last :"+lst);
        if(evt.getKeyCode()!=KeyEvent.VK_SHIFT)
        lister(lst,passf1.getX(),passf1.getY());
        else SCP.setVisible(false);
        //}
        */
    }//GEN-LAST:event_passf1KeyReleased

    private void saveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveActionPerformed
if(passf1.getText().equals(passf2.getText())){
        if(validity(passf1.getText())){
            System.out.println("String value "+StringValue(passf1.getText()));
            String passexptd=passValue(StringValue(passf1.getText()));
            String entpass=new String(passckr.getPassword());
            if(entpass.equals(passexptd)){
                try {
                    ReplaceLineWN.mainn(Add+"/"+usr, "profile.txt", 2, passf1.getText());
                    this.dispose();
                } catch (IOException ex) {
                    Logger.getLogger(NP.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println("You have passed the all thing all you dynamic setted ::)");
            }else{
                System.out.println("Entered pass:"+entpass+"   Expected:"+passexptd);
                JLabel label = new JLabel("Password is incorrect !!!");
    label.setFont(new Font("Arial", Font.BOLD, 18));
    JOptionPane.showMessageDialog(this,label,"ERROR",JOptionPane.WARNING_MESSAGE);
            }
        }else{
            JLabel label = new JLabel("Password formate is not valid !!!");
    label.setFont(new Font("Arial", Font.BOLD, 18));
    JOptionPane.showMessageDialog(this,label,"ERROR",JOptionPane.WARNING_MESSAGE);
        }
}else{
    JLabel label = new JLabel("Format are not matching with each other");
    label.setFont(new Font("Arial", Font.BOLD, 18));
    JOptionPane.showMessageDialog(this,label,"ERROR",JOptionPane.WARNING_MESSAGE);
}
    }//GEN-LAST:event_saveActionPerformed

    private void passf1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_passf1KeyTyped
        
    }//GEN-LAST:event_passf1KeyTyped

    private void passf1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_passf1FocusLost
        
    }//GEN-LAST:event_passf1FocusLost

    private void passckrFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_passckrFocusGained
        passf1.setEditable(false);
        passf2.setEditable(false);
    }//GEN-LAST:event_passckrFocusGained

    private void passf1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_passf1KeyPressed
         if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            passf2.requestFocus();
        }
    }//GEN-LAST:event_passf1KeyPressed

    private void passf2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_passf2KeyPressed
         if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            passckr.requestFocus();
        }
    }//GEN-LAST:event_passf2KeyPressed

    private void passckrKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_passckrKeyPressed
         if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            save.requestFocus();
        }
    }//GEN-LAST:event_passckrKeyPressed
/*public void lister(String str,int x , int y){
    DefaultTableModel model = (DefaultTableModel)table.getModel();
    model.setRowCount(0);
        SCP.setVisible(false);
        //SCP.setBounds(x, y, 100, 200);
        try {
            Scanner scn = new Scanner(new File("D:\\Shopy\\Shopy/charset.txt"));
            while(scn.hasNext()){
                String srt = scn.next();
                if(srt.contains(str)){
        
        model.insertRow(table.getRowCount(),new Object[]{srt});
        
        }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(rough.class.getName()).log(Level.SEVERE, null, ex);
        }
        
}*/
public boolean checkrep(String cd){
    boolean res = false;
    try {
            Scanner scn = new Scanner(new File("D:\\Shopy\\Shopy/charset.txt"));
            while(scn.hasNext()){
                String str=scn.next();
                
                str=str.substring(1,str.length()-1);
                ////System.out.println("Sub:"+str+"  Res:"+cd.compareToIgnoreCase(str)+"  cd:"+cd);
               if(cd.compareToIgnoreCase(str)==0){
                   res=true;
                   break;
               }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(rough.class.getName()).log(Level.SEVERE, null, ex);
        }
    //System.out.println("CheckRep for: "+cd+" ::"+res);
    return(res);
}
public boolean ismathematical(String cd){
    boolean res=false;
    if(cd.compareToIgnoreCase("+")==0||(cd.compareToIgnoreCase("-")==0||cd.compareToIgnoreCase("*")==0))
        res=true;
    //System.out.println("Is Mathematical for: "+cd+" ::"+res);
    return res;
}
public boolean mathematicalvalidity(String Formatstr,String tgstr,int ind){
   //System.out.println("Format::"+Formatstr+" Tagstr:::"+tgstr+"  ind::::"+ind);
    boolean res= true;
    String str1="",str2="";
    for(int l=ind-3-tgstr.length();l>=0;--l){
        //System.out.println("Charcter at ::"+l+" ::"+Formatstr.charAt(l));
        if(Formatstr.charAt(l)=='<'){
            //System.out.println("Mathe:::"+str1);
            if(!checkrep(str1)) res=false;
            break;
        }else{
            str1=Formatstr.charAt(l)+str1;
        }
    }
    for(int l=ind+2;l<Formatstr.length();++l){
        if(Formatstr.charAt(l)=='>'){
            if(!checkrep(str2)) res=false;
            break;
        }else{
            str2=str2+Formatstr.charAt(l);
        }
    }
    //System.out.println("MathematicalValidity for: "+tgstr+" ::"+res);
    return res;
}
public boolean validity(String str){        //<dd><MM><+><yyyy>
    boolean res= true;
    for(int i=0;i<str.length();++i){
        char a = str.charAt(i);
        if(a=='<'){ //New tag is going on...
            String nstr="";
            for(++i;i<str.length();++i){
            if(str.charAt(i)!='>'){
                nstr+=str.charAt(i);
            }else{
                if(checkrep(nstr)){ //Atleast code valid
                if(ismathematical(nstr)){
                    if(!mathematicalvalidity(str,nstr,i)){
                    res=false; 
                    //System.out.println("mathematically Not correct");
                    }else{
                        //System.out.println("mathematically correct");
                    }
                    
                }
                }
                else{//Code is not valid
                    res=false;
                    //System.out.println(nstr+"  Code is not valid");
                }
               break;//Single tag/code reading completed
            }
        }
        }
    }
    return res;
}

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(NP.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NP.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NP.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NP.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

       java.awt.EventQueue.invokeLater(new Runnable() {
           
            public void run() {
                
                new NP("121").setVisible(true);
                
            }
        }); 

       
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancel;
    private javax.swing.JButton clear;
    private javax.swing.JLabel crtm;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JPasswordField passckr;
    private javax.swing.JTextField passf1;
    private javax.swing.JTextField passf2;
    private javax.swing.JButton save;
    private javax.swing.JLabel tmlbl;
    // End of variables declaration//GEN-END:variables
}
