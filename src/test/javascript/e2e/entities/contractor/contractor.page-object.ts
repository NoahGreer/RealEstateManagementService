import { element, by, ElementFinder } from 'protractor';

export class ContractorComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-contractor div table .btn-danger'));
  title = element.all(by.css('jhi-contractor div h2#page-heading span')).first();
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

export class ContractorUpdatePage {
  pageTitle = element(by.id('jhi-contractor-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  companyNameInput = element(by.id('field_companyName'));
  streetAddressInput = element(by.id('field_streetAddress'));
  cityInput = element(by.id('field_city'));
  stateCodeInput = element(by.id('field_stateCode'));
  zipCodeInput = element(by.id('field_zipCode'));
  phoneNumberInput = element(by.id('field_phoneNumber'));
  contactPersonInput = element(by.id('field_contactPerson'));
  ratingOfWorkInput = element(by.id('field_ratingOfWork'));
  ratingOfResponsivenessInput = element(by.id('field_ratingOfResponsiveness'));

  jobTypeSelect = element(by.id('field_jobType'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setCompanyNameInput(companyName: string): Promise<void> {
    await this.companyNameInput.sendKeys(companyName);
  }

  async getCompanyNameInput(): Promise<string> {
    return await this.companyNameInput.getAttribute('value');
  }

  async setStreetAddressInput(streetAddress: string): Promise<void> {
    await this.streetAddressInput.sendKeys(streetAddress);
  }

  async getStreetAddressInput(): Promise<string> {
    return await this.streetAddressInput.getAttribute('value');
  }

  async setCityInput(city: string): Promise<void> {
    await this.cityInput.sendKeys(city);
  }

  async getCityInput(): Promise<string> {
    return await this.cityInput.getAttribute('value');
  }

  async setStateCodeInput(stateCode: string): Promise<void> {
    await this.stateCodeInput.sendKeys(stateCode);
  }

  async getStateCodeInput(): Promise<string> {
    return await this.stateCodeInput.getAttribute('value');
  }

  async setZipCodeInput(zipCode: string): Promise<void> {
    await this.zipCodeInput.sendKeys(zipCode);
  }

  async getZipCodeInput(): Promise<string> {
    return await this.zipCodeInput.getAttribute('value');
  }

  async setPhoneNumberInput(phoneNumber: string): Promise<void> {
    await this.phoneNumberInput.sendKeys(phoneNumber);
  }

  async getPhoneNumberInput(): Promise<string> {
    return await this.phoneNumberInput.getAttribute('value');
  }

  async setContactPersonInput(contactPerson: string): Promise<void> {
    await this.contactPersonInput.sendKeys(contactPerson);
  }

  async getContactPersonInput(): Promise<string> {
    return await this.contactPersonInput.getAttribute('value');
  }

  async setRatingOfWorkInput(ratingOfWork: string): Promise<void> {
    await this.ratingOfWorkInput.sendKeys(ratingOfWork);
  }

  async getRatingOfWorkInput(): Promise<string> {
    return await this.ratingOfWorkInput.getAttribute('value');
  }

  async setRatingOfResponsivenessInput(ratingOfResponsiveness: string): Promise<void> {
    await this.ratingOfResponsivenessInput.sendKeys(ratingOfResponsiveness);
  }

  async getRatingOfResponsivenessInput(): Promise<string> {
    return await this.ratingOfResponsivenessInput.getAttribute('value');
  }

  async jobTypeSelectLastOption(): Promise<void> {
    await this.jobTypeSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async jobTypeSelectOption(option: string): Promise<void> {
    await this.jobTypeSelect.sendKeys(option);
  }

  getJobTypeSelect(): ElementFinder {
    return this.jobTypeSelect;
  }

  async getJobTypeSelectedOption(): Promise<string> {
    return await this.jobTypeSelect.element(by.css('option:checked')).getText();
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

export class ContractorDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-contractor-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-contractor'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
