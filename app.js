// --- Landing Page Interactive Features ---

let currentFontSize = 100;
function resizeFont(action) {
    if (action === 'increase' && currentFontSize < 130) currentFontSize += 10;
    else if (action === 'decrease' && currentFontSize > 80) currentFontSize -= 10;
    else if (action === 'reset') currentFontSize = 100;
    
    document.body.style.fontSize = currentFontSize + '%';
}

function switchWidgetTab(tabName) {
    const pnrTab = document.getElementById('tab-pnr');
    const chartsTab = document.getElementById('tab-charts');
    const formContent = document.getElementById('widget-form-content');
    const heading = document.getElementById('widget-heading');

    if (tabName === 'pnr') {
        pnrTab.classList.add('active');
        chartsTab.classList.remove('active');
        heading.innerText = "BOOK TICKET";
        formContent.style.display = 'block';
    } else {
        chartsTab.classList.add('active');
        pnrTab.classList.remove('a.ctive');
        heading.innerText = "CHART / VACANCY";
        formContent.innerHTML = `
            <div class="form-row" style="margin-top:20px;">
                <div class="input-box full-width" style="width:100%; display:flex; align-items:center; border:1px solid #ccc; padding:10px; border-radius:4px;">
                    <i class="fas fa-train" style="color:#213d77; margin-right:10px;"></i>
                    <input type="text" placeholder="Enter Train Number (e.g. 12581)" style="border:none; outline:none; width:100%;">
                </div>
            </div>
            <p style="font-size:0.8rem; color:#64748b; text-align:center; margin-top:15px;">View live segment-wise vacant berths across stations instantly.</p>
        `;
    }
}

function swapStations() {
    const fromInput = document.getElementById('landing-from');
    const toInput = document.getElementById('landing-to');
    const temp = fromInput.value;
    fromInput.value = toInput.value;
    toInput.value = temp;
}

// --- Auth Modal Logic (Login vs Register) ---
let currentAuthMode = 'login';

function openAuthModal(mode) {
    currentAuthMode = mode;
    document.getElementById('auth-modal').style.display = 'flex';
    switchAuthMode(mode);
}

function closeAuthModal() {
    document.getElementById('auth-modal').style.display = 'none';
}

function switchAuthMode(mode) {
    currentAuthMode = mode;
    const loginTab = document.getElementById('modal-tab-login');
    const registerTab = document.getElementById('modal-tab-register');
    const title = document.getElementById('auth-title');
    const subtitle = document.getElementById('auth-subtitle');
    const submitBtn = document.getElementById('auth-submit-btn');
    const confirmPassInput = document.getElementById('auth-pass-confirm');

    if (mode === 'login') {
        loginTab.classList.add('active');
        registerTab.classList.remove('active');
        title.innerText = "RailFlex Secure Login";
        subtitle.innerText = "Enter your credentials to access smart reservations.";
        submitBtn.innerText = "Login";
        confirmPassInput.style.display = 'none';
    } else {
        registerTab.classList.add('active');
        loginTab.classList.remove('active');
        title.innerText = "Create Account";
        subtitle.innerText = "Register to experience smart railway segment booking.";
        submitBtn.innerText = "Register";
        confirmPassInput.style.display = 'block';
    }
}

function submitAuth() {
    const user = document.getElementById('auth-user').value;
    const pass = document.getElementById('auth-pass').value;

    if (!user || !pass) {
        alert("Please fill in all required fields.");
        return;
    }

    if (currentAuthMode === 'register') {
        const confirmPass = document.getElementById('auth-pass-confirm').value;
        if (pass !== confirmPass) {
            alert("Passwords do not match!");
            return;
        }
        alert(`Account successfully created for ${user}! You are now logged in.`);
    } else {
        alert(`Welcome back, ${user}! Successfully logged in.`);
    }

    // Update Upper Right Corner Profile Section
    document.getElementById('nav-username').innerText = user;
    document.querySelector('.profile-info small').innerText = "Active User";

    closeAuthModal();
}

function googleSignIn() {
    alert("Redirecting to Google Secure Authentication Gateway...");
    setTimeout(() => {
        alert("Authentication successful via Google Account!");
        document.getElementById('nav-username').innerText = "Google User";
        document.querySelector('.profile-info small').innerText = "Verified";
    }, 500);
}

// --- Promotional Slider Functionality ---
let slideIndex = 0;
let slideInterval;

function showSlides() {
    const slides = document.querySelectorAll('.slide');
    const dots = document.querySelectorAll('.dot');
    
    if (slides.length === 0) return;

    slides.forEach(slide => slide.classList.remove('active'));
    dots.forEach(dot => dot.classList.remove('active'));

    slideIndex++;
    if (slideIndex > slides.length) { slideIndex = 1; }

    slides[slideIndex - 1].classList.add('active');
    dots[slideIndex - 1].classList.add('active');
}

function startSlider() {
    slideInterval = setInterval(showSlides, 4000);
}

function moveSlide(n) {
    clearInterval(slideInterval);
    const slides = document.querySelectorAll('.slide');
    const dots = document.querySelectorAll('.dot');
    
    slides.forEach(slide => slide.classList.remove('active'));
    dots.forEach(dot => dot.classList.remove('active'));

    slideIndex += n;
    if (slideIndex > slides.length) { slideIndex = 1; }
    if (slideIndex < 1) { slideIndex = slides.length; }

    slides[slideIndex - 1].classList.add('active');
    dots[slideIndex - 1].classList.add('active');
    startSlider();
}

function currentSlide(n) {
    clearInterval(slideInterval);
    const slides = document.querySelectorAll('.slide');
    const dots = document.querySelectorAll('.dot');
    
    slides.forEach(slide => slide.classList.remove('active'));
    dots.forEach(dot => dot.classList.remove('active'));

    slideIndex = n;
    slides[slideIndex - 1].classList.add('active');
    dots[slideIndex - 1].classList.add('active');
    startSlider();
}

function handleSlideClick(featureName) {
    if (featureName === 'Smart Allocation') {
        alert("Redirecting to RailFlex Dynamic Segment Matrix...");
        goToBooking();
    } else {
        alert(`Opening details for: ${featureName}`);
    }
}

window.addEventListener('DOMContentLoaded', () => {
    startSlider();
});

// --- Transition to Booking Page ---
function goToBooking() {
    const fromVal = document.getElementById('landing-from').value;
    const toVal = document.getElementById('landing-to').value;
    
    document.getElementById('landing-page').style.display = 'none';
    document.getElementById('booking-page').style.display = 'flex';
    window.scrollTo({ top: 0, behavior: 'smooth' });
    
    if (fromVal) document.getElementById('source').value = fromVal;
    if (toVal) document.getElementById('destination').value = toVal;
}

// --- Booking Page Interactive Features ---
function searchTrains() {
    const source = document.getElementById('source').value;
    const dest = document.getElementById('destination').value;
    const btn = document.querySelector('.search-btn');
    
    if (!source || !dest) {
        alert("Please enter both FROM and TO stations.");
        return;
    }
    
    const originalText = btn.innerText;
    btn.innerText = "Checking Segments...";
    
    setTimeout(() => {
        btn.innerText = originalText;
        alert(`Segment Matrix Verified! Optimized routing applied between ${source} and ${dest}.`);
    }, 600);
}

function selectSeat(seatElement) {
    if (seatElement.classList.contains('available')) {
        seatElement.classList.remove('available');
        seatElement.classList.add('short'); 
    } 
    else if (seatElement.classList.contains('short')) {
        seatElement.classList.remove('short');
        seatElement.classList.add('medium'); 
    }
    else if (seatElement.classList.contains('medium')) {
        seatElement.classList.remove('medium');
        seatElement.classList.add('booked'); 
    }
    else {
        alert("Seat fully booked across intersecting segments.");
    }
}

function switchCoach(carElement, coachCode) {
    document.querySelectorAll('.train-car.coach').forEach(c => c.classList.remove('active'));
    carElement.classList.add('active');
    
    const badge = document.querySelector('.badge');
    badge.innerText = coachCode.startsWith('A') ? '2A' : (coachCode.startsWith('B') ? '3A' : 'GEN');
    
    document.querySelectorAll('.seat').forEach(seat => {
        seat.className = 'seat available';
    });
}
// --- Holiday Cards Click Handler ---
function handleHolidayClick(packageName) {
    if (packageName === 'Maharajas Express' || packageName === 'Kerala Backwaters') {
        alert(`Opening exclusive itinerary and seat matrix for: ${packageName}`);
        goToBooking(); // Automatically forwards the user to your interactive seat reservation page!
    } else {
        alert(`Exploring curated packages for: ${packageName}`);
        goToBooking();
    }
}