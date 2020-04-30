import { element, by, ElementFinder } from 'protractor';

export class PropertyTaxComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-property-tax div table .btn-danger'));
  title = element.all(by.css('jhi-property-tax div h2#page-heading span')).first();
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

export class PropertyTaxUpdatePage {
  pageTitle = element(by.id('jhi-property-tax-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  taxYearInput = element(by.id('field_taxYear'));
  amountInput = element(by.id('field_amount'));
  datePaidInput = element(by.id('field_datePaid'));
  confirmationNumberInput = element(by.id('field_confirmationNumber'));

  buildingSelect = element(by.id('field_building'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setTaxYearInput(taxYear: string): Promise<void> {
    await this.taxYearInput.sendKeys(taxYear);
  }

  async getTaxYearInput(): Promise<string> {
    return await this.taxYearInput.getAttribute('value');
  }

  async setAmountInput(amount: string): Promise<void> {
    await this.amountInput.sendKeys(amount);
  }

  async getAmountInput(): Promise<string> {
    return await this.amountInput.getAttribute('value');
  }

  async setDatePaidInput(datePaid: string): Promise<void> {
    await this.datePaidInput.sendKeys(datePaid);
  }

  async getDatePaidInput(): Promise<string> {
    return await this.datePaidInput.getAttribute('value');
  }

  async setConfirmationNumberInput(confirmationNumber: string): Promise<void> {
    await this.confirmationNumberInput.sendKeys(confirmationNumber);
  }

  async getConfirmationNumberInput(): Promise<string> {
    return await this.confirmationNumberInput.getAttribute('value');
  }

  async buildingSelectLastOption(): Promise<void> {
    await this.buildingSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async buildingSelectOption(option: string): Promise<void> {
    await this.buildingSelect.sendKeys(option);
  }

  getBuildingSelect(): ElementFinder {
    return this.buildingSelect;
  }

  async getBuildingSelectedOption(): Promise<string> {
    return await this.buildingSelect.element(by.css('option:checked')).getText();
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

export class PropertyTaxDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-propertyTax-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-propertyTax'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
