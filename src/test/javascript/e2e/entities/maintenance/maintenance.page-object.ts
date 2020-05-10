import { element, by, ElementFinder } from 'protractor';

export class MaintenanceComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-maintenance div table .btn-danger'));
  title = element.all(by.css('jhi-maintenance div h2#page-heading span')).first();
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

export class MaintenanceUpdatePage {
  pageTitle = element(by.id('jhi-maintenance-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  descriptionInput = element(by.id('field_description'));
  notificationDateInput = element(by.id('field_notificationDate'));
  dateCompleteInput = element(by.id('field_dateComplete'));
  contractorJobNumberInput = element(by.id('field_contractorJobNumber'));
  repairCostInput = element(by.id('field_repairCost'));
  repairPaidOnInput = element(by.id('field_repairPaidOn'));
  receiptOfPaymentInput = element(by.id('field_receiptOfPayment'));

  apartmentSelect = element(by.id('field_apartment'));
  contractorSelect = element(by.id('field_contractor'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setDescriptionInput(description: string): Promise<void> {
    await this.descriptionInput.sendKeys(description);
  }

  async getDescriptionInput(): Promise<string> {
    return await this.descriptionInput.getAttribute('value');
  }

  async setNotificationDateInput(notificationDate: string): Promise<void> {
    await this.notificationDateInput.sendKeys(notificationDate);
  }

  async getNotificationDateInput(): Promise<string> {
    return await this.notificationDateInput.getAttribute('value');
  }

  async setDateCompleteInput(dateComplete: string): Promise<void> {
    await this.dateCompleteInput.sendKeys(dateComplete);
  }

  async getDateCompleteInput(): Promise<string> {
    return await this.dateCompleteInput.getAttribute('value');
  }

  async setContractorJobNumberInput(contractorJobNumber: string): Promise<void> {
    await this.contractorJobNumberInput.sendKeys(contractorJobNumber);
  }

  async getContractorJobNumberInput(): Promise<string> {
    return await this.contractorJobNumberInput.getAttribute('value');
  }

  async setRepairCostInput(repairCost: string): Promise<void> {
    await this.repairCostInput.sendKeys(repairCost);
  }

  async getRepairCostInput(): Promise<string> {
    return await this.repairCostInput.getAttribute('value');
  }

  async setRepairPaidOnInput(repairPaidOn: string): Promise<void> {
    await this.repairPaidOnInput.sendKeys(repairPaidOn);
  }

  async getRepairPaidOnInput(): Promise<string> {
    return await this.repairPaidOnInput.getAttribute('value');
  }

  async setReceiptOfPaymentInput(receiptOfPayment: string): Promise<void> {
    await this.receiptOfPaymentInput.sendKeys(receiptOfPayment);
  }

  async getReceiptOfPaymentInput(): Promise<string> {
    return await this.receiptOfPaymentInput.getAttribute('value');
  }

  async apartmentSelectLastOption(): Promise<void> {
    await this.apartmentSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async apartmentSelectOption(option: string): Promise<void> {
    await this.apartmentSelect.sendKeys(option);
  }

  getApartmentSelect(): ElementFinder {
    return this.apartmentSelect;
  }

  async getApartmentSelectedOption(): Promise<string> {
    return await this.apartmentSelect.element(by.css('option:checked')).getText();
  }

  async contractorSelectLastOption(): Promise<void> {
    await this.contractorSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async contractorSelectOption(option: string): Promise<void> {
    await this.contractorSelect.sendKeys(option);
  }

  getContractorSelect(): ElementFinder {
    return this.contractorSelect;
  }

  async getContractorSelectedOption(): Promise<string> {
    return await this.contractorSelect.element(by.css('option:checked')).getText();
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

export class MaintenanceDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-maintenance-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-maintenance'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
