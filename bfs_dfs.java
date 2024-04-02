import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

class Node {
    private int value;
    private Node left;
    private Node right;

    Node(int value) {
        this.value = value;
    }

    int getValue() {
        return value;
    }

    Node getLeft() {
        return left;
    }

    void setLeft(Node left) {
        this.left = left;
    }

    Node getRight() {
        return right;
    }

    void setRight(Node right) {
        this.right = right;
    }
}

public class bfs_dfs extends JFrame {

    private BST bst;
    private JPanel treePanel;
    private JTextField insertTextField;
    private JButton insertButton, deleteButton, bfsButton, dfsButton;
    private JTextArea traversalTextArea;

    private Node currentTraversedNode;

    bfs_dfs() {
        setTitle("bfs_dfs");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        bst = new BST();

        initComponents();
    }

    private void initComponents() {
        treePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawTree(g, bst.getRoot(), getWidth() / 2, 50, getWidth() / 4, currentTraversedNode);
            }
        };

        insertTextField = new JTextField(10);

        insertButton = new JButton("Insert");
        insertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String valueStr = insertTextField.getText();
                if (!valueStr.isEmpty()) {
                    int value = Integer.parseInt(valueStr);
                    bst.insert(value);
                    treePanel.repaint();
                    insertTextField.setText("");
                } else {
                    JOptionPane.showMessageDialog(bfs_dfs.this, "Please enter a valid value.");
                }
            }
        });

        deleteButton = new JButton("Delete Tree");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bst.clear();
                treePanel.repaint();
                traversalTextArea.setText("");
            }
        });

        bfsButton = new JButton("BFS");
        bfsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bfsTraversal();
            }
        });

        dfsButton = new JButton("DFS");
        dfsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dfsTraversal();
            }
        });

        traversalTextArea = new JTextArea(10, 20);
        traversalTextArea.setEditable(false);

        JPanel controlPanel = new JPanel();
        controlPanel.add(new JLabel("Insert Value:"));
        controlPanel.add(insertTextField);
        controlPanel.add(insertButton);
        controlPanel.add(deleteButton);
        controlPanel.add(bfsButton);
        controlPanel.add(dfsButton);

        JScrollPane scrollPane = new JScrollPane(traversalTextArea);

        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.add(new JLabel("Traversal Path:"), BorderLayout.NORTH);
        textPanel.add(scrollPane, BorderLayout.CENTER);

        setLayout(new BorderLayout());
        add(controlPanel, BorderLayout.NORTH);
        add(treePanel, BorderLayout.CENTER);
        add(textPanel, BorderLayout.EAST);
    }

    private void drawTree(Graphics g, Node node, int x, int y, int xOffset, Node current) {
        if (node != null) {
            int circleDiameter = 30;
            int circleRadius = circleDiameter / 2;
            int leftChildX = x - xOffset;
            int leftChildY = y + 50;
            int rightChildX = x + xOffset;
            int rightChildY = y + 50;
            g.setColor(Color.WHITE);
            g.fillOval(x - 15, y - 15, 30, 30);
            if (node == current) {
                g.setColor(Color.GREEN);
            } else {
                g.setColor(Color.BLACK);
            }
            g.drawOval(x - 15, y - 15, 30, 30);
            g.drawString(String.valueOf(node.getValue()), x - 5, y + 5);
            if (node.getLeft() != null) {
                g.drawLine(x, y + circleRadius, leftChildX, leftChildY - circleRadius);
                drawTree(g, node.getLeft(), x - xOffset, y + 50, xOffset / 2, current);
            }
            if (node.getRight() != null) {
                g.drawLine(x, y + circleRadius, rightChildX, rightChildY - circleRadius);
                drawTree(g, node.getRight(), x + xOffset, y + 50, xOffset / 2, current);
            }
        }
    }

    private void bfsTraversal() {
        StringBuilder sb = new StringBuilder();
        Queue<Node> queue = new LinkedList<>();
        queue.add(bst.getRoot());
        while (!queue.isEmpty()) {
            Node current = queue.poll();
            sb.append(current.getValue()).append(" ");
            currentTraversedNode = current;
            treePanel.repaint();
            if (current.getLeft() != null) {
                queue.add(current.getLeft());
            }
            if (current.getRight() != null) {
                queue.add(current.getRight());
            }
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        traversalTextArea.setText(sb.toString());
        currentTraversedNode = null;
        treePanel.repaint();
    }

    private void dfsTraversal() {
        StringBuilder sb = new StringBuilder();
        Stack<Node> stack = new Stack<>();
        stack.push(bst.getRoot());
        while (!stack.isEmpty()) {
            Node current = stack.pop();
            sb.append(current.getValue()).append(" ");
            currentTraversedNode = current;
            treePanel.repaint();
            if (current.getRight() != null) {
                stack.push(current.getRight());
            }
            if (current.getLeft() != null) {
                stack.push(current.getLeft());
            }
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        traversalTextArea.setText(sb.toString());
        currentTraversedNode = null;
        treePanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new bfs_dfs().setVisible(true);
            }
        });
    }
}

class BST {
    private Node root;

    void insert(int value) {
        root = insert(root, value);
    }

    private Node insert(Node node, int value) {
        if (node == null) {
            return new Node(value);
        }
        if (value < node.getValue()) {
            node.setLeft(insert(node.getLeft(), value));
        } else if (value > node.getValue()) {
            node.setRight(insert(node.getRight(), value));
        }
        return node;
    }

    void clear() {
        root = null;
    }

    Node getRoot() {
        return root;
    }
}
