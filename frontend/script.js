let employees = JSON.parse(localStorage.getItem("employees")) || [];

const form = document.getElementById("employee-form");
const table = document.getElementById("employee-table");
const payslipDiv = document.getElementById("payslip");
const payslipSection = document.getElementById("payslip-section");
const closePayslip = document.getElementById("closePayslip");

// Display all employees on page load
displayEmployees();

form.addEventListener("submit", (e) => {
  e.preventDefault();
  const id = parseInt(document.getElementById("empId").value);
  const name = document.getElementById("empName").value;
  const dept = document.getElementById("empDept").value;
  const basic = parseFloat(document.getElementById("empBasic").value);

  const existing = employees.find((emp) => emp.id === id);
  if (existing) {
    existing.name = name;
    existing.department = dept;
    existing.basicSalary = basic;
    alert("Employee updated successfully!");
  } else {
    employees.push({ id, name, department: dept, basicSalary: basic });
    alert("Employee added successfully!");
  }

  saveEmployees();
  displayEmployees();
  form.reset();
});

function displayEmployees() {
  table.innerHTML = "";
  employees.forEach((emp) => {
    const row = document.createElement("tr");
    row.innerHTML = `
      <td>${emp.id}</td>
      <td>${emp.name}</td>
      <td>${emp.department}</td>
      <td>${emp.basicSalary.toFixed(2)}</td>
      <td>
        <button class="action-btn edit" onclick="editEmployee(${emp.id})">Edit</button>
        <button class="action-btn delete" onclick="deleteEmployee(${emp.id})">Delete</button>
        <button class="action-btn payslip" onclick="showPayslip(${emp.id})">Payslip</button>
      </td>
    `;
    table.appendChild(row);
  });
}

function editEmployee(id) {
  const emp = employees.find((e) => e.id === id);
  if (!emp) return;
  document.getElementById("empId").value = emp.id;
  document.getElementById("empName").value = emp.name;
  document.getElementById("empDept").value = emp.department;
  document.getElementById("empBasic").value = emp.basicSalary;
}

function deleteEmployee(id) {
  if (confirm("Delete this employee?")) {
    employees = employees.filter((e) => e.id !== id);
    saveEmployees();
    displayEmployees();
  }
}

function saveEmployees() {
  localStorage.setItem("employees", JSON.stringify(employees));
}

function showPayslip(id) {
  const emp = employees.find((e) => e.id === id);
  if (!emp) return;

  const basic = emp.basicSalary;
  const hra = basic * 0.2;
  const da = basic * 0.1;
  const gross = basic + hra + da;
  const pf = basic * 0.12;
  const professionalTax = 200;
  const taxable = gross - pf - professionalTax;
  const tax = computeTax(taxable);
  const net = gross - pf - professionalTax - tax;

  payslipDiv.innerHTML = `
    <h3>${emp.name} (${emp.department})</h3>
    <p><b>Basic:</b> ₹${basic.toFixed(2)}</p>
    <p><b>HRA:</b> ₹${hra.toFixed(2)}</p>
    <p><b>DA:</b> ₹${da.toFixed(2)}</p>
    <p><b>Gross:</b> ₹${gross.toFixed(2)}</p>
    <p><b>PF:</b> ₹${pf.toFixed(2)}</p>
    <p><b>Professional Tax:</b> ₹${professionalTax.toFixed(2)}</p>
    <p><b>Income Tax:</b> ₹${tax.toFixed(2)}</p>
    <p><b>Net Pay:</b> ₹${net.toFixed(2)}</p>
  `;
  payslipSection.style.display = "block";
}

function computeTax(monthly) {
  const annual = monthly * 12;
  let tax = 0;
  if (annual <= 250000) tax = 0;
  else if (annual <= 500000) tax = (annual - 250000) * 0.05;
  else if (annual <= 1000000) tax = (250000 * 0.05) + (annual - 500000) * 0.2;
  else tax = (250000 * 0.05) + (500000 * 0.2) + (annual - 1000000) * 0.3;
  return tax / 12;
}

closePayslip.addEventListener("click", () => {
  payslipSection.style.display = "none";
});
