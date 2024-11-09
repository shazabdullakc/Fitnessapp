import javax.swing.*;
import java.awt.*;

public class FitnessPlanner extends JFrame {
    private JTextField nameField, ageField, weightField, heightField;
    private JComboBox<String> goalCombo, activityLevelCombo;
    private JRadioButton maleButton, femaleButton;
    private JTextArea resultArea;

    public FitnessPlanner() {
        setTitle("Fitness Planner");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        setSize(600, 800);

        // Input Panel
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(8, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Personal Information
        inputPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Age:"));
        ageField = new JTextField();
        inputPanel.add(ageField);

        inputPanel.add(new JLabel("Weight (kg):"));
        weightField = new JTextField();
        inputPanel.add(weightField);

        inputPanel.add(new JLabel("Height (cm):"));
        heightField = new JTextField();
        inputPanel.add(heightField);

        // Gender Selection
        inputPanel.add(new JLabel("Gender:"));
        JPanel genderPanel = new JPanel();
        ButtonGroup genderGroup = new ButtonGroup();
        maleButton = new JRadioButton("Male");
        femaleButton = new JRadioButton("Female");
        genderGroup.add(maleButton);
        genderGroup.add(femaleButton);
        genderPanel.add(maleButton);
        genderPanel.add(femaleButton);
        inputPanel.add(genderPanel);

        // Goal Selection
        inputPanel.add(new JLabel("Goal:"));
        String[] goals = {"Weight Loss", "Muscle Gain", "Maintenance"};
        goalCombo = new JComboBox<>(goals);
        inputPanel.add(goalCombo);

        // Activity Level
        inputPanel.add(new JLabel("Activity Level:"));
        String[] activityLevels = {"Sedentary", "Lightly Active", "Moderately Active", "Very Active"};
        activityLevelCombo = new JComboBox<>(activityLevels);
        inputPanel.add(activityLevelCombo);

        // Generate Button
        JButton generateButton = new JButton("Generate Plan");
        generateButton.addActionListener(e -> generatePlan());
        inputPanel.add(generateButton);

        // Result Area
        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setWrapStyleWord(true);
        resultArea.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(resultArea);

        // Add components to frame
        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void generatePlan() {
        try {
            // Get user inputs
            int age = Integer.parseInt(ageField.getText());
            double weight = Double.parseDouble(weightField.getText());
            double height = Double.parseDouble(heightField.getText());
            String goal = (String) goalCombo.getSelectedItem();
            String activityLevel = (String) activityLevelCombo.getSelectedItem();
            boolean isMale = maleButton.isSelected();

            // Calculate BMR (Basal Metabolic Rate) using Harris-Benedict equation
            double bmr;
            if (isMale) {
                bmr = 88.362 + (13.397 * weight) + (4.799 * height) - (5.677 * age);
            } else {
                bmr = 447.593 + (9.247 * weight) + (3.098 * height) - (4.330 * age);
            }

            // Calculate TDEE (Total Daily Energy Expenditure)
            double activityMultiplier = switch (activityLevel) {
                case "Sedentary" -> 1.2;
                case "Lightly Active" -> 1.375;
                case "Moderately Active" -> 1.55;
                case "Very Active" -> 1.725;
                default -> 1.2;
            };

            double tdee = bmr * activityMultiplier;

            // Adjust calories based on goal
            double targetCalories = switch (goal) {
                case "Weight Loss" -> tdee - 500;
                case "Muscle Gain" -> tdee + 500;
                default -> tdee;
            };

            // Generate workout plan
            StringBuilder workoutPlan = new StringBuilder();
            workoutPlan.append("WORKOUT PLAN:\n\n");

            switch (goal) {
                case "Weight Loss" -> generateWeightLossPlan(workoutPlan);
                case "Muscle Gain" -> generateMusclePlan(workoutPlan);
                default -> generateMaintenancePlan(workoutPlan);
            }

            // Generate diet plan
            StringBuilder dietPlan = new StringBuilder();
            dietPlan.append("\nDIET PLAN:\n\n");
            dietPlan.append(String.format("Daily Calorie Target: %.0f calories\n\n", targetCalories));
            generateDietPlan(dietPlan, targetCalories);

            // Display results
            resultArea.setText(workoutPlan.toString() + dietPlan.toString());

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numeric values for age, weight, and height.");
        }
    }

    private void generateWeightLossPlan(StringBuilder plan) {
        plan.append("Monday: Cardio + Full Body\n");
        plan.append("- 30 min moderate intensity cardio\n");
        plan.append("- 3x12 Squats\n");
        plan.append("- 3x12 Push-ups\n");
        plan.append("- 3x12 Dumbbell rows\n\n");

        plan.append("Wednesday: HIIT + Core\n");
        plan.append("- 20 min HIIT intervals\n");
        plan.append("- 3x20 Crunches\n");
        plan.append("- 3x30s Planks\n");
        plan.append("- 3x15 Mountain climbers\n\n");

        plan.append("Friday: Circuit Training\n");
        plan.append("- 3 rounds of:\n");
        plan.append("  * 1 min Jump rope\n");
        plan.append("  * 15 Burpees\n");
        plan.append("  * 20 Lunges\n");
        plan.append("  * 15 Push-ups\n");
    }

    private void generateMusclePlan(StringBuilder plan) {
        plan.append("Monday: Push Day\n");
        plan.append("- 4x8-12 Bench Press\n");
        plan.append("- 4x8-12 Shoulder Press\n");
        plan.append("- 3x12-15 Tricep Extensions\n\n");

        plan.append("Wednesday: Pull Day\n");
        plan.append("- 4x8-12 Barbell Rows\n");
        plan.append("- 4x8-12 Pull-ups/Lat Pulldowns\n");
        plan.append("- 3x12-15 Bicep Curls\n\n");

        plan.append("Friday: Leg Day\n");
        plan.append("- 4x8-12 Squats\n");
        plan.append("- 4x8-12 Romanian Deadlifts\n");
        plan.append("- 3x12-15 Leg Press\n");
    }

    private void generateMaintenancePlan(StringBuilder plan) {
        plan.append("Monday: Upper Body\n");
        plan.append("- 3x10 Push-ups\n");
        plan.append("- 3x10 Dumbbell rows\n");
        plan.append("- 20 min light cardio\n\n");

        plan.append("Wednesday: Lower Body\n");
        plan.append("- 3x10 Bodyweight squats\n");
        plan.append("- 3x10 Lunges\n");
        plan.append("- 20 min light cardio\n\n");

        plan.append("Friday: Full Body\n");
        plan.append("- 3x10 of each:\n");
        plan.append("  * Push-ups\n");
        plan.append("  * Squats\n");
        plan.append("  * Planks\n");
    }

    private void generateDietPlan(StringBuilder plan, double calories) {
        double protein = calories * 0.3 / 4; // 30% of calories from protein
        double carbs = calories * 0.45 / 4;  // 45% of calories from carbs
        double fats = calories * 0.25 / 9;   // 25% of calories from fats

        plan.append(String.format("Macronutrient Breakdown:\n"));
        plan.append(String.format("- Protein: %.0fg\n", protein));
        plan.append(String.format("- Carbohydrates: %.0fg\n", carbs));
        plan.append(String.format("- Fats: %.0fg\n\n", fats));

        plan.append("Sample Meal Plan:\n\n");
        plan.append("Breakfast:\n");
        plan.append("- Oatmeal with protein powder\n");
        plan.append("- Banana\n");
        plan.append("- Greek yogurt\n\n");

        plan.append("Lunch:\n");
        plan.append("- Grilled chicken breast\n");
        plan.append("- Brown rice\n");
        plan.append("- Mixed vegetables\n\n");

        plan.append("Dinner:\n");
        plan.append("- Lean protein (fish/chicken/lean beef)\n");
        plan.append("- Sweet potato\n");
        plan.append("- Green vegetables\n\n");

        plan.append("Snacks:\n");
        plan.append("- Protein shake\n");
        plan.append("- Nuts\n");
        plan.append("- Fruit\n");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new FitnessPlanner().setVisible(true);
        });
    }
}