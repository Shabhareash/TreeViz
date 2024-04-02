import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

class BSTVisualizer extends JFrame {

    private BST tree;
    private List<BSTNode> highlightedNodes; 

    private JPanel treePanel;
    private JTextField insertField, deleteField, searchField;
    private JButton insertButton, deleteButton, searchButton, inorderButton, preorderButton, postorderButton;
    public JTextArea traversalTextArea;

    BSTVisualizer() {
        setTitle("BSTVisualizer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        tree = new BST();
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
                    JOptionPane.showMessageDialog(BSTVisualizer.this, "Please enter a valid integer value.");
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
                    JOptionPane.showMessageDialog(BSTVisualizer.this, "Please enter a valid integer value.");
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
                    JOptionPane.showMessageDialog(BSTVisualizer.this, "Please enter a valid integer value.");
                }
            }
        });

        inorderButton = new JButton("Inorder");
        inorderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inorder();
            }
        });

        preorderButton = new JButton("Preorder");
        preorderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                preorder();
            }
        });

        postorderButton = new JButton("Postorder");
        postorderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                postorder();
            }
        });

        traversalTextArea = new JTextArea(10, 20);
        traversalTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(traversalTextArea);

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
        controlPanel.add(inorderButton);
        controlPanel.add(preorderButton);
        controlPanel.add(postorderButton);

        setLayout(new BorderLayout());
        add(controlPanel, BorderLayout.NORTH);
        add(treePanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);
    }

    private void drawTree(Graphics g, int x, int y, BSTNode node, int xOffset) {
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
        BSTNode foundNode = tree.searchNode(value);
        if (foundNode != null) {
            traversalTextArea.append(value + " found in the BST.\n");
            highlightPath(foundNode);
        } else {
            traversalTextArea.append(value + " not found in the BST.\n");
        }
    }

    private void highlightPath(BSTNode node) {
        
        while (node != null) {
            highlightedNodes.add(node);
            node = node.getParent(); 
        }
    }

    private void inorder() {
        traversalTextArea.append("Inorder Traversal: ");
        tree.inorderTraversal(traversalTextArea);
        traversalTextArea.append("\n");
    }

    private void preorder() {
        traversalTextArea.append("Preorder Traversal: ");
        tree.preorderTraversal(traversalTextArea);
        traversalTextArea.append("\n");
    }

    private void postorder() {
        traversalTextArea.append("Postorder Traversal: ");
        tree.postorderTraversal(traversalTextArea);
        traversalTextArea.append("\n");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new BSTVisualizer().setVisible(true);
            }
        });
    }
}

class BSTNode {
    private int value;
    private BSTNode left;
    private BSTNode right;
    private BSTNode parent; 

    BSTNode(int value) {
        this.value = value;
    }

    int getValue() {
        return value;
    }

    BSTNode getLeft() {
        return left;
    }

    BSTNode getRight() {
        return right;
    }

    BSTNode getParent() {
        return parent;
    }

    void setLeft(BSTNode left) {
        this.left = left;
        if (left != null) {
            left.parent = this; 
        }
    }

    void setRight(BSTNode right) {
        this.right = right;
        if (right != null) {
            right.parent = this; 
        }
    }

    public void setValue(int minValue) {
        throw new UnsupportedOperationException("Unimplemented method 'setValue'");
    }
}

class BST {
    private BSTNode root;

    BSTNode getRoot() {
        return root;
    }

    void insert(int value) {
        root = insert(root, value);
    }

    private BSTNode insert(BSTNode node, int value) {
        if (node == null)
            return new BSTNode(value);

        if (value < node.getValue())
            node.setLeft(insert(node.getLeft(), value));
        else if (value > node.getValue())
            node.setRight(insert(node.getRight(), value));

        return node;
    }

    void delete(int value) {
        root = delete(root, value);
    }

    private BSTNode delete(BSTNode node, int value) {
        if (node == null)
            return node;

        if (value < node.getValue())
            node.setLeft(delete(node.getLeft(), value));
        else if (value > node.getValue())
            node.setRight(delete(node.getRight(), value));
        else {
            if (node.getLeft() == null)
                return node.getRight();
            else if (node.getRight() == null)
                return node.getLeft();

            node.setValue(minValue(node.getRight()));

            node.setRight(delete(node.getRight(), node.getValue()));
        }
        return node;
    }

    private int minValue(BSTNode node) {
        int minv = node.getValue();
        while (node.getLeft() != null) {
            minv = node.getLeft().getValue();
            node = node.getLeft();
        }
        return minv;
    }

    boolean search(int value) {
        return search(root, value);
    }

    BSTNode searchNode(int value) {
        return searchNode(root, value);
    }

    private boolean search(BSTNode node, int value) {
        if (node == null)
            return false;
        if (node.getValue() == value)
            return true;
        if (value < node.getValue())
            return search(node.getLeft(), value);
        else
            return search(node.getRight(), value);
    }

    private BSTNode searchNode(BSTNode node, int value) {
        if (node == null)
            return null;
        if (node.getValue() == value)
            return node;
        if (value < node.getValue())
            return searchNode(node.getLeft(), value);
        else
            return searchNode(node.getRight(), value);
    }
    void inorderTraversal(JTextArea traversalTextArea) {
        inorderTraversal(root, traversalTextArea);
    }
    
    private void inorderTraversal(BSTNode node, JTextArea traversalTextArea) {
        if (node != null) {
            inorderTraversal(node.getLeft(), traversalTextArea);
            traversalTextArea.append(node.getValue() + " "); 
            try {
                Thread.sleep(500); 
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            inorderTraversal(node.getRight(), traversalTextArea);
        }
    }
    
    void preorderTraversal(JTextArea traversalTextArea) {
        preorderTraversal(root, traversalTextArea);
    }
    
    private void preorderTraversal(BSTNode node, JTextArea traversalTextArea) {
        if (node != null) {
            traversalTextArea.append(node.getValue() + " "); 
            try {
                Thread.sleep(500); 
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            preorderTraversal(node.getLeft(), traversalTextArea);
            preorderTraversal(node.getRight(), traversalTextArea);
        }
    }
    
    void postorderTraversal(JTextArea traversalTextArea) {
        postorderTraversal(root, traversalTextArea);
    }
    
    private void postorderTraversal(BSTNode node, JTextArea traversalTextArea) {
        if (node != null) {
            postorderTraversal(node.getLeft(), traversalTextArea);
            postorderTraversal(node.getRight(), traversalTextArea);
            traversalTextArea.append(node.getValue() + " "); 
            try {
                Thread.sleep(500); 
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
