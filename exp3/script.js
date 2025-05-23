function validateForm() {
  const requiredFields = [
    "name",
    "phone",
    "age",
    "email",
    "address",
    "city",
    "state",
    "zip",
    "country",
  ];
  for (let field of requiredFields) {
    if (!document.getElementById(field).value.trim()) {
      alert("All fields are required.");
      return false;
    }
  }

  const phone = document.getElementById("phone").value.trim();
  if (phone.length !== 10 || isNaN(phone)) {
    alert("Enter a valid 10-digit phone number.");
    return false;
  }

  const age = document.getElementById("age").value.trim();
  if (isNaN(age) || age < 16 || age > 40) {
    alert("Age must be between 16 and 40.");
    return false;
  }

  const email = document.getElementById("email").value.trim();
  if (!email.includes("@") || !email.includes(".")) {
    alert("Enter a valid email address.");
    return false;
  }

  const gender = document.querySelector('input[name="gender"]:checked');
  if (!gender) {
    alert("Please select a gender.");
    return false;
  }

  alert("Form submitted successfully!");
  return true;
}
