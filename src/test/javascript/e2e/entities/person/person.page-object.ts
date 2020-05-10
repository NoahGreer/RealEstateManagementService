import { element, by, ElementFinder } from 'protractor';

export class PersonComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-person div table .btn-danger'));
  title = element.all(by.css('jhi-person div h2#page-heading span')).first();
  noResult = element(by.id('no-result'));
  entities = element(by.id('entities'));

  async clickOnCreateButton(): Promise<void> {
    await this.createButton.click();
  }

  async clickOnLastDeleteButton(): Promise<void> {
    await this.deleteButtons.last().click();
  }

  async countDeleteButtons(): Promise<number> {
    return this.deleteButtons.count();
  }

  async getTitle(): Promise<string> {
    return this.title.getAttribute('jhiTranslate');
  }
}

export class PersonUpdatePage {
  pageTitle = element(by.id('jhi-person-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  firstNameInput = element(by.id('field_firstName'));
  lastNameInput = element(by.id('field_lastName'));
  phoneNumberInput = element(by.id('field_phoneNumber'));
  emailAddressInput = element(by.id('field_emailAddress'));
  primaryContactInput = element(by.id('field_primaryContact'));
  isMinorInput = element(by.id('field_isMinor'));
  ssnInput = element(by.id('field_ssn'));
  backgroundCheckDateInput = element(by.id('field_backgroundCheckDate'));
  backgroundCheckConfirmationNumberInput = element(by.id('field_backgroundCheckConfirmationNumber'));
  employmentVerificationDateInput = element(by.id('field_employmentVerificationDate'));
  employmentVerificationConfirmationNumberInput = element(by.id('field_employmentVerificationConfirmationNumber'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setFirstNameInput(firstName: string): Promise<void> {
    await this.firstNameInput.sendKeys(firstName);
  }

  async getFirstNameInput(): Promise<string> {
    return await this.firstNameInput.getAttribute('value');
  }

  async setLastNameInput(lastName: string): Promise<void> {
    await this.lastNameInput.sendKeys(lastName);
  }

  async getLastNameInput(): Promise<string> {
    return await this.lastNameInput.getAttribute('value');
  }

  async setPhoneNumberInput(phoneNumber: string): Promise<void> {
    await this.phoneNumberInput.sendKeys(phoneNumber);
  }

  async getPhoneNumberInput(): Promise<string> {
    return await this.phoneNumberInput.getAttribute('value');
  }

  async setEmailAddressInput(emailAddress: string): Promise<void> {
    await this.emailAddressInput.sendKeys(emailAddress);
  }

  async getEmailAddressInput(): Promise<string> {
    return await this.emailAddressInput.getAttribute('value');
  }

  getPrimaryContactInput(): ElementFinder {
    return this.primaryContactInput;
  }

  getIsMinorInput(): ElementFinder {
    return this.isMinorInput;
  }

  async setSsnInput(ssn: string): Promise<void> {
    await this.ssnInput.sendKeys(ssn);
  }

  async getSsnInput(): Promise<string> {
    return await this.ssnInput.getAttribute('value');
  }

  async setBackgroundCheckDateInput(backgroundCheckDate: string): Promise<void> {
    await this.backgroundCheckDateInput.sendKeys(backgroundCheckDate);
  }

  async getBackgroundCheckDateInput(): Promise<string> {
    return await this.backgroundCheckDateInput.getAttribute('value');
  }

  async setBackgroundCheckConfirmationNumberInput(backgroundCheckConfirmationNumber: string): Promise<void> {
    await this.backgroundCheckConfirmationNumberInput.sendKeys(backgroundCheckConfirmationNumber);
  }

  async getBackgroundCheckConfirmationNumberInput(): Promise<string> {
    return await this.backgroundCheckConfirmationNumberInput.getAttribute('value');
  }

  async setEmploymentVerificationDateInput(employmentVerificationDate: string): Promise<void> {
    await this.employmentVerificationDateInput.sendKeys(employmentVerificationDate);
  }

  async getEmploymentVerificationDateInput(): Promise<string> {
    return await this.employmentVerificationDateInput.getAttribute('value');
  }

  async setEmploymentVerificationConfirmationNumberInput(employmentVerificationConfirmationNumber: string): Promise<void> {
    await this.employmentVerificationConfirmationNumberInput.sendKeys(employmentVerificationConfirmationNumber);
  }

  async getEmploymentVerificationConfirmationNumberInput(): Promise<string> {
    return await this.employmentVerificationConfirmationNumberInput.getAttribute('value');
  }

  async save(): Promise<void> {
    await this.saveButton.click();
  }

  async cancel(): Promise<void> {
    await this.cancelButton.click();
  }

  getSaveButton(): ElementFinder {
    return this.saveButton;
  }
}

export class PersonDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-person-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-person'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
