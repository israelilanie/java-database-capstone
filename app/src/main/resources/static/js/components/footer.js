/*
  Function to render the footer content into the page

  This file defines a reusable footer component that is injected
  dynamically into any page containing a <div id="footer"></div>.
*/

function renderFooter() {
  // Select the footer container from the DOM
  const footer = document.getElementById("footer");
  if (!footer) return;

  // Inject footer HTML content
  footer.innerHTML = `
    <footer class="footer">
      <div class="footer-container">

        <!-- Branding Section -->
        <div class="footer-logo">
          <img src="../assets/images/logo/logo.png" alt="Hospital CMS Logo">
          <p>© Copyright 2025. All Rights Reserved by Hospital CMS.</p>
        </div>

        <!-- Footer Links -->
        <div class="footer-links">

          <!-- Company Links -->
          <div class="footer-column">
            <h4>Company</h4>
            <a href="#">About</a>
            <a href="#">Careers</a>
            <a href="#">Press</a>
          </div>

          <!-- Support Links -->
          <div class="footer-column">
            <h4>Support</h4>
            <a href="#">Account</a>
            <a href="#">Help Center</a>
            <a href="#">Contact Us</a>
          </div>

          <!-- Legal Links -->
          <div class="footer-column">
            <h4>Legals</h4>
            <a href="#">Terms & Conditions</a>
            <a href="#">Privacy Policy</a>
            <a href="#">Licensing</a>
          </div>

        </div>
      </div>
    </footer>
  `;
}

// Render footer on page load
renderFooter();
