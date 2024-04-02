import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

class AVLTreeVisualizer extends JFrame {

    private AVLTree tree;
    private List<AVLNode> highlightedNodes; 

    private JPanel treePanel;
    private JTextField insertField, deleteField, searchField;
    private JButton insertButton, deleteButton, searchButton;
    public JTextArea traversalTextArea;

    AVLTreeVisualizer() {
        setTitle("AVLTreeVisualizer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        tree = new AVLTree();
        highlightedNodes = new ArrayList<>(); 

        initComponents();
    }

    private void initComponents() {
        treePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawTree(g, getWidth() / 2, 50, tree.getRoot(), 200);
            }
        };

        insertField = new JTextField(10);
        insertButton = new JButton("Insert");
        insertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int value = Integer.parseInt(insertField.getText());
                    tree.insert(value);
                    insertField.setText("");
                    treePanel.repaint();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(AVLTreeVisualizer.this, "Please enter a valid integer value.");
                }
            }
        });

        deleteField = new JTextField(10);
        deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int value = Integer.parseInt(deleteField.getText());
                    tree.delete(value);
                    deleteField.setText("");
                    treePanel.repaint();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(AVLTreeVisualizer.this, "Please enter a valid integer value.");
                }
            }
        });

        searchField = new JTextField(10);
        searchButton = new JButton("Search");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int searchValue = Integer.parseInt(searchField.getText());
                    search(searchValue);
                    treePanel.repaint();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(AVLTreeVisualizer.this, "Please enter a valid integer value.");
                }
            }
        });

        JPanel controlPanel = new JPanel();
        controlPanel.add(new JLabel("Insert Value:"));
        controlPanel.add(insertField);
        controlPanel.add(insertButton);
        controlPanel.add(new JLabel("Delete Value:"));
        controlPanel.add(deleteField);
        controlPanel.add(deleteButton);
        controlPanel.add(new JLabel("Search Value:"));
        controlPanel.add(searchField);
        controlPanel.add(searchButton);

        setLayout(new BorderLayout());
        add(controlPanel, BorderLayout.NORTH);
        add(treePanel, BorderLayout.CENTER);
    }

    private void drawTree(Graphics g, int x, int y, AVLNode node, int xOffset) {
        if (node != null) {
            int circleDiameter = 30;
            int circleRadius = circleDiameter / 2;

            g.drawOval(x - circleRadius, y - circleRadius, circleDiameter, circleDiameter);
            g.drawString(String.valueOf(node.getValue()), x - 5, y + 5);

            if (node.getLeft() != null) {
                int leftChildX = x - xOffset;
                int leftChildY = y + 50;
                Color originalColor = g.getColor();
                if (highlightedNodes.contains(node.getLeft())) { 
                    g.setColor(Color.GREEN);
                }
                g.drawLine(x, y + circleRadius, leftChildX, leftChildY - circleRadius);
                g.setColor(originalColor);
                drawTree(g, leftChildX, leftChildY, node.getLeft(), xOffset / 2);
            }
            if (node.getRight() != null) {
                int rightChildX = x + xOffset;
                int rightChildY = y + 50;
                Color originalColor = g.getColor();
                if (highlightedNodes.contains(node.getRight())) { 
                    g.setColor(Color.GREEN);
                }
                g.drawLine(x, y + circleRadius, rightChildX, rightChildY - circleRadius);
                g.setColor(originalColor);
                drawTree(g, rightChildX, rightChildY, node.getRight(), xOffset / 2);
            }
        }
    }

    private void search(int value) {
        highlightedNodes.clear(); 
        AVLNode foundNode = tree.searchNode(value);
        if (foundNode != null) {
            JOptionPane.showMessageDialog(AVLTreeVisualizer.this, value + " found in the AVL tree.");
            highlightPath(foundNode);
        } else {
            JOptionPane.showMessageDialog(AVLTreeVisualizer.this, value + " not found in the AVL tree.");
        }
    }
    private void highlightPath(AVLNode node) {
        highlightedNodes.clear(); 
        
        while (node != null && node.getParent() != null && !highlightedNodes.contains(node.getParent())) {
            highlightedNodes.add(node);
            node = node.getParent(); 
        }
    }
    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new AVLTreeVisualizer().setVisible(true);
            }
        });
    }
}

class AVLNode {
    private int value;
    private AVLNode left;
    private AVLNode right;
    private AVLNode parent; 
    private int height; 

    AVLNode(int value) {
        this.value = value;
        this.height = 1;
    }

    int getValue() {
        return value;
    }

    AVLNode getLeft() {
        return left;
    }

    AVLNode getRight() {
        return right;
    }

    AVLNode getParent() {
        return parent;
    }

    int getHeight() {
        return height;
    }

    void setLeft(AVLNode left) {
        this.left = left;
        if (left != null) {
            left.parent = this; 
        }
    }

    void setRight(AVLNode right) {
        this.right = right;
        if (right != null) {
            right.parent = this; 
        }
    }

    void setValue(int value) {
        this.value = value;
    }

    void setHeight(int height) {
        this.height = height;
    }
}

class AVLTree extends JFrame{
    private AVLNode root;

    AVLNode getRoot() {
        return root;
    }

    void insert(int value) {
        root = insert(root, value);
    }

    private AVLNode insert(AVLNode node, int value) {
        if (node == null)
            return new AVLNode(value);

        if (value < node.getValue())
            node.setLeft(insert(node.getLeft(), value));
        else if (value > node.getValue())
            node.setRight(insert(node.getRight(), value));

        node.setHeight(1 + Math.max(height(node.getLeft()), height(node.getRight())));

        return balance(node, value);
    }

    void delete(int value) {
        root = delete(root, value);
    }

    private AVLNode delete(AVLNode root, int value) {
        if (root == null)
            return root;

        if (value < root.getValue())
            root.setLeft(delete(root.getLeft(), value));
        else if (value > root.getValue())
            root.setRight(delete(root.getRight(), value));
        else {
            if ((root.getLeft() == null) || (root.getRight() == null)) {
                AVLNode temp = null;
                if (temp == root.getLeft())
                    temp = root.getRight();
                else
                    temp = root.getLeft();

                if (temp == null) {
                    temp = root;
                    root = null;
                } else 
                    root = temp; 
            } else {
                AVLNode temp = minValueNode(root.getRight());
                root.setValue(temp.getValue());
                root.setRight(delete(root.getRight(), temp.getValue()));
            }
        }

        if (root == null)
            return root;

        root.setHeight(1 + Math.max(height(root.getLeft()), height(root.getRight())));

        return balance(root, value);
    }

    private AVLNode balance(AVLNode node, int value) {
        
        int balance = getBalance(node);

        if (balance > 1 && value < node.getLeft().getValue())
            return rightRotate(node);
        if (balance < -1 && value > node.getRight().getValue())
            return leftRotate(node);
        if (balance > 1 && value > node.getLeft().getValue()) {
            node.setLeft(leftRotate(node.getLeft()));
            return rightRotate(node);
        }
        if (balance < -1 && value < node.getRight().getValue()) {
            node.setRight(rightRotate(node.getRight()));
            return leftRotate(node);
        }

        return node;
    }

    private AVLNode minValueNode(AVLNode node) {
        AVLNode current = node;
        while (current.getLeft() != null)
            current = current.getLeft();
        return current;
    }

    boolean search(int value) {
        return search(root, value);
    }

    private boolean search(AVLNode root, int value) {
        if (root == null)
            return false;
        if (root.getValue() == value)
            return true;
        if (value < root.getValue())
            return search(root.getLeft(), value);
        else
            return search(root.getRight(), value);
    }

    AVLNode searchNode(int value) {
        return searchNode(root, value);
    }

    private AVLNode searchNode(AVLNode root, int value) {
        if (root == null)
            return null;
        if (root.getValue() == value)
            return root;
        if (value < root.getValue())
            return searchNode(root.getLeft(), value);
        else
            return searchNode(root.getRight(), value);
    }

    private int height(AVLNode node) {
        if (node == null)
            return 0;
        return node.getHeight();
    }

    private int getBalance(AVLNode node) {
        if (node == null)
            return 0;
        return height(node.getLeft()) - height(node.getRight());
    }

    private AVLNode rightRotate(AVLNode y) {
        AVLNode x = y.getLeft();
        AVLNode T2 = x.getRight();

        x.setRight(y);
        y.setLeft(T2);

        y.setHeight(Math.max(height(y.getLeft()), height(y.getRight())) + 1);
        x.setHeight(Math.max(height(x.getLeft()), height(x.getRight())) + 1);

        return x;
    }

    private AVLNode leftRotate(AVLNode x) {
        AVLNode y = x.getRight();
        AVLNode T2 = y.getLeft();

        y.setLeft(x);
        x.setRight(T2);

        x.setHeight(Math.max(height(x.getLeft()), height(x.getRight())) + 1);
        y.setHeight(Math.max(height(y.getLeft()), height(y.getRight())) + 1);

        return y;
    }
}
