import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainApp extends JFrame {
    public MainApp() {
        setTitle("Tree Visualization App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null); // Center the frame on the screen

        initComponents();
    }

    private void initComponents() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1)); // Menu with 4 rows

        JButton btnBST = new JButton("BST Visualizer");
        btnBST.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openProgram("BSTVisualizer");
            }
        });
        panel.add(btnBST);

        JButton btnAVLTree = new JButton("AVL Tree Visualizer");
        btnAVLTree.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openProgram("AVLTreeVisualizer");
            }
        });
        panel.add(btnAVLTree);

        JButton btnBFS_DFS = new JButton("BFS/DFS Visualizer");
        btnBFS_DFS.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openProgram("bfs_dfs");
            }
        });
        panel.add(btnBFS_DFS);

        // Add the panel to the frame
        add(panel);
    }

    private void openProgram(String programName) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    Class<?> programClass = Class.forName(programName);
                    JFrame programInstance = (JFrame) programClass.newInstance();
                    programInstance.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Set custom close operation
                    programInstance.setVisible(true);
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainApp().setVisible(true);
            }
        });
    }
}
