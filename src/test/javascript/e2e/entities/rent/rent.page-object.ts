import { element, by, ElementFinder } from 'protractor';

export class RentComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-rent div table .btn-danger'));
  title = element.all(by.css('jhi-rent div h2#page-heading span')).first();
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

export class RentUpdatePage {
  pageTitle = element(by.id('jhi-rent-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  dueDateInput = element(by.id('field_dueDate'));
  receivedDateInput = element(by.id('field_receivedDate'));
  amountInput = element(by.id('field_amount'));

  leaseSelect = element(by.id('field_lease'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setDueDateInput(dueDate: string): Promise<void> {
    await this.dueDateInput.sendKeys(dueDate);
  }

  async getDueDateInput(): Promise<string> {
    return await this.dueDateInput.getAttribute('value');
  }

  async setReceivedDateInput(receivedDate: string): Promise<void> {
    await this.receivedDateInput.sendKeys(receivedDate);
  }

  async getReceivedDateInput(): Promise<string> {
    return await this.receivedDateInput.getAttribute('value');
  }

  async setAmountInput(amount: string): Promise<void> {
    await this.amountInput.sendKeys(amount);
  }

  async getAmountInput(): Promise<string> {
    return await this.amountInput.getAttribute('value');
  }

  async leaseSelectLastOption(): Promise<void> {
    await this.leaseSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async leaseSelectOption(option: string): Promise<void> {
    await this.leaseSelect.sendKeys(option);
  }

  getLeaseSelect(): ElementFinder {
    return this.leaseSelect;
  }

  async getLeaseSelectedOption(): Promise<string> {
    return await this.leaseSelect.element(by.css('option:checked')).getText();
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

export class RentDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-rent-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-rent'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
