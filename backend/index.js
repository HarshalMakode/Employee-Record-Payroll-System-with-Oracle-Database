import express from "express";
import cors from "cors";
import bodyParser from "body-parser";
import dotenv from "dotenv";
import employeeRoutes from "./routes/employeeRoutes.js";
import payrollRoutes from "./routes/payrollRoutes.js";

dotenv.config();
const app = express();

app.use(cors());
app.use(bodyParser.json());

app.get("/", (req, res) => {
  res.send("Employee Record & Payroll System Backend Running ✅");
});

app.use("/employees", employeeRoutes);
app.use("/payroll", payrollRoutes);

const PORT = process.env.PORT || 8000;
app.listen(PORT, () => console.log(`Server running on port ${PORT}`));
