package org.jdvn.lis.valuation.user;

import javax.validation.Valid;

import org.jdvn.lis.valuation.domain.User;
import org.jdvn.lis.valuation.exception.DuplicateEmailException;
import org.jdvn.lis.valuation.exception.DuplicateLoginIdException;
import org.jdvn.lis.valuation.service.SignupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/signup")
public class SignupController {

	public static final String FORM_MODEL_KEY = "form";
	public static final String ERRORS_MODEL_KEY = BindingResult.MODEL_KEY_PREFIX + FORM_MODEL_KEY;

	@Autowired
	private SignupService signupService;

	@ModelAttribute(FORM_MODEL_KEY)
	public SignupForm setupSignupForm() {
		return new SignupForm();
	}

	@RequestMapping(method = RequestMethod.GET)
	public String init(Model model) {
		SignupForm form = new SignupForm();
		model.addAttribute(FORM_MODEL_KEY, form);
		return edit(model);
	}

	@RequestMapping(method = RequestMethod.GET, params = "step.edit")
	public String edit(Model model) {
		return "signup";
	}

	@RequestMapping(method=RequestMethod.POST)
	public String signup(
			@Valid @ModelAttribute("form") SignupForm form,
			BindingResult errors,
			RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute(FORM_MODEL_KEY, form);
		redirectAttributes.addFlashAttribute(ERRORS_MODEL_KEY, errors);

		if (errors.hasErrors()) {
			return "redirect:/signup?step.edit";
		}

		try {
			signupService.signup(form.toSignupRequest(), User.Role.ACTIVITI_USER);
		} catch (DuplicateLoginIdException e) {
			errors.rejectValue("loginId", "NotDuplicate");
			return "redirect:/signup?step.edit";
		} catch (DuplicateEmailException e) {
			errors.rejectValue("email", "NotDuplicate");
			return "redirect:/signup?step.edit";
		}

		redirectAttributes.getFlashAttributes().clear();
		return "redirect:/";
	}
}
