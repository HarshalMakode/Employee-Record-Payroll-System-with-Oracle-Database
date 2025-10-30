import express from "express";
import { getConnection, rowsToObjects } from "../db.js";

const router = express.Router();

// Perform payroll transaction (bonus + salary update)
router.post("/process", async (req, res) => {
  const { employeeId, bonus } = req.body;
  let connection;
  try {
    connection = await getConnection();
    await connection.execute("BEGIN NULL; END;"); // start transaction implicitly

    // Fetch salary
    const result = await connection.execute(
      "SELECT salary FROM employees WHERE employee_id = :id",
      { id: employeeId }
    );

    if (result.rows.length === 0) throw new Error("Employee not found");

    const oldSalary = result.rows[0][0];
    const newSalary = oldSalary + bonus;

    await connection.execute(
      "UPDATE employees SET salary = :newSalary WHERE employee_id = :id",
      { newSalary, id: employeeId }
    );

    await connection.commit();
    res.json({
      message: "Payroll processed successfully!",
      employeeId,
      oldSalary,
      newSalary,
    });
  } catch (err) {
    if (connection) await connection.rollback();
    res.status(500).json({ error: "Transaction failed: " + err.message });
  } finally {
    if (connection) await connection.close();
  }
});

export default router;
