import express from "express";
import { getConnection, rowsToObjects } from "../db.js";
import oracledb from "oracledb";

const router = express.Router();

router.get("/", async (req, res) => {
  let connection;
  try {
    connection = await getConnection();
    const result = await connection.execute("SELECT * FROM employees");
    res.json(rowsToObjects(result));
  } catch (err) {
    res.status(500).json({ error: err.message });
  } finally {
    if (connection) await connection.close();
  }
});

// POST add employee
router.post("/", async (req, res) => {
  const { name, designation, salary } = req.body;
  let connection;
  try {
    connection = await getConnection();
    const result = await connection.execute(
      `INSERT INTO employees (name, designation, salary)
       VALUES (:name, :designation, :salary)
       RETURNING employee_id INTO :id`,
      {
        name,
        designation,
        salary,
        id: { dir: oracledb.BIND_OUT, type: oracledb.NUMBER },
      },
      { autoCommit: true }
    );
    res.json({
      employee_id: result.outBinds.id[0],
      name,
      designation,
      salary,
    });
  } catch (err) {
    res.status(500).json({ error: err.message });
  } finally {
    if (connection) await connection.close();
  }
});

export default router;
